package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.dto.ClientInfoDTO;
import dev.enricosola.porcellino.dto.auth.TwoFactorCodeChallengeDTO;
import dev.enricosola.porcellino.dto.auth.TwoFactorRecoveryCodeChallengeDTO;
import dev.enricosola.porcellino.exception.NotEnabledTwoFactorAuthException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import dev.enricosola.porcellino.events.auth.AuthRenewCompletedEvent;
import dev.enricosola.porcellino.exception.UserNotActiveException;
import dev.enricosola.porcellino.support.AuthenticatedUserDetails;
import org.springframework.security.core.AuthenticationException;
import dev.enricosola.porcellino.events.auth.AuthCompletedEvent;
import dev.enricosola.porcellino.support.AuthenticationContract;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.events.auth.AuthRevokedEvent;
import dev.enricosola.porcellino.events.auth.AuthFailedEvent;
import org.springframework.context.ApplicationEventPublisher;
import dev.enricosola.porcellino.exception.NotFoundException;
import dev.enricosola.porcellino.support.AuthTokenKeychain;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.enums.AuthFailReason;
import dev.enricosola.porcellino.dto.user.UserAuthDTO;
import dev.enricosola.porcellino.entity.RefreshToken;
import dev.enricosola.porcellino.dto.UserTokenDTO;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {
    protected static final String[] TWO_FACTOR_AUTH_SCOPES = {"2fa"};
    protected static final String[] AUTH_SCOPES = {"auth"};

    protected final ApplicationEventPublisher applicationEventPublisher;
    protected final AuthenticationManager authenticationManager;
    protected final TwoFactorAuthService twoFactorAuthService;
    protected final RefreshTokenService refreshTokenService;
    protected final RecoveryCodeService recoveryCodeService;
    protected final AccessTokenService accessTokenService;
    protected final UserLookupService userLookupService;

    public AuthenticationService(
            ApplicationEventPublisher applicationEventPublisher,
            AuthenticationManager authenticationManager,
            TwoFactorAuthService twoFactorAuthService,
            RefreshTokenService refreshTokenService,
            RecoveryCodeService recoveryCodeService,
            AccessTokenService accessTokenService,
            UserLookupService userLookupService
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.authenticationManager = authenticationManager;
        this.twoFactorAuthService = twoFactorAuthService;
        this.refreshTokenService = refreshTokenService;
        this.recoveryCodeService = recoveryCodeService;
        this.accessTokenService = accessTokenService;
        this.userLookupService = userLookupService;
    }

    /**
     * Authenticates a user based on the provided credentials and generates an authentication contract.
     *
     * @param userAuthDTO The data transfer object containing the user's authentication credentials (email and password).
     * @return An AuthenticationContract consisting of user information and a keychain with access and refresh tokens.
     * @throws UsernameNotFoundException If no user matching the given email address is found.
     */
    @Transactional
    public AuthenticationContract authenticate(UserAuthDTO userAuthDTO) {
        try {
            User user = this.userLookupService.findActiveUserByEmail(userAuthDTO.getEmail());
            String[] scopes = user.is2FAEnabled() ? AuthenticationService.TWO_FACTOR_AUTH_SCOPES : AuthenticationService.AUTH_SCOPES;
            AuthenticatedUserDetails authenticatedUserDetails = new AuthenticatedUserDetails(user);
            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticatedUserDetails,
                    userAuthDTO.getPassword(),
                    authenticatedUserDetails.getAuthorities()
            ));
            return this.finalizeAuthentication(authentication, user, scopes, userAuthDTO.getClientInfoDTO());
        } catch (AuthenticationException ex) {
            this.applicationEventPublisher.publishEvent(new AuthFailedEvent(this, AuthFailReason.INVALID_CREDENTIALS, userAuthDTO.getEmail()));
            log.info("Failed authentication attempt while authenticating user \"{}\" (INVALID_CREDENTIALS).", userAuthDTO.getEmail());
            throw ex;
        }  catch(UserNotActiveException ex) {
            this.applicationEventPublisher.publishEvent(new AuthFailedEvent(this, AuthFailReason.USER_NOT_ACTIVATED, userAuthDTO.getEmail()));
            log.info("Failed authentication attempt while authenticating user \"{}\" (USER_NOT_ACTIVATED).", userAuthDTO.getEmail());
            throw ex;
        } catch (NotFoundException ex) {
            this.applicationEventPublisher.publishEvent(new AuthFailedEvent(this, AuthFailReason.USER_NOT_FOUND, userAuthDTO.getEmail()));
            log.info("Failed authentication attempt while authenticating user \"{}\" (USER_NOT_FOUND).", userAuthDTO.getEmail());
            throw new UsernameNotFoundException("No user matching the given email address found.", ex);
        }
    }

    /**
     *
     */
    public AuthenticationContract challenge2FACode(int userId, TwoFactorCodeChallengeDTO twoFactorCodeChallengeDTO) {
        User user = this.userLookupService.find(userId);
        if ( !user.is2FAEnabled() ) {
            throw new NotEnabledTwoFactorAuthException("2FA is not enabled.");
        }
        this.twoFactorAuthService.check(user.getTwoFactorAuthSecret(), twoFactorCodeChallengeDTO.getCode());
        AuthenticatedUserDetails authenticatedUserDetails = new AuthenticatedUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUserDetails,
                null,
                authenticatedUserDetails.getAuthorities()
        );
        return this.finalizeAuthentication(authentication, user, AuthenticationService.AUTH_SCOPES, twoFactorCodeChallengeDTO.getClientInfoDTO());
    }

    public AuthenticationContract challenge2FARecoveryCode(int userId, TwoFactorRecoveryCodeChallengeDTO twoFactorRecoveryCodeChallengeDTO) {
        User user = this.userLookupService.find(userId);
        if ( !user.is2FAEnabled() ) {
            throw new NotEnabledTwoFactorAuthException("2FA is not enabled.");
        }
        this.recoveryCodeService.findByCodeAndInvalidate(userId, twoFactorRecoveryCodeChallengeDTO.getRecoveryCode());
        AuthenticatedUserDetails authenticatedUserDetails = new AuthenticatedUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUserDetails,
                null,
                authenticatedUserDetails.getAuthorities()
        );
        return this.finalizeAuthentication(authentication, user, AuthenticationService.AUTH_SCOPES, twoFactorRecoveryCodeChallengeDTO.getClientInfoDTO());
    }

    /**
     * Retrieves the authenticated user associated with the provided authentication token.
     *
     * @param authentication The authentication object containing user credentials and details.
     * @return The authenticated User entity retrieved from the user lookup service.
     * @throws UsernameNotFoundException If the user cannot be found or is not active.
     */
    public User getAuthenticatedUser(Authentication authentication) {
        try {
            AuthenticatedUserDetails authenticatedUserDetails = (AuthenticatedUserDetails)authentication.getPrincipal();
            return this.userLookupService.findActiveUserByEmail(authenticatedUserDetails.getUsername());
        } catch (NotFoundException ex) {
            throw new UsernameNotFoundException("User not found.", ex);
        }
    }

    /**
     * Refreshes the access token using the provided refresh token. Optionally rotates the refresh token.
     *
     * @param refreshToken The refresh token used to generate a new access token.
     * @param rotateRefreshToken A flag indicating whether the refresh token should be rotated.
     * @return An AuthTokenKeychain object containing the new access token and, if applicable, the rotated refresh token.
     */
    public AuthTokenKeychain refreshAccessToken(String refreshToken, boolean rotateRefreshToken) {
        UserTokenDTO refreshTokenDTO = this.refreshTokenService.unpack(refreshToken);
        UserTokenDTO accessTokenDTO = this.accessTokenService.generate(refreshTokenDTO.getUser());
        if ( rotateRefreshToken ) {
            RefreshToken rotatedRefreshToken = this.refreshTokenService.findAndRotate(refreshToken);
            refreshTokenDTO = UserTokenDTO.builder()
                    .token(rotatedRefreshToken.getToken())
                    .expiration(rotatedRefreshToken.getExpiredAt())
                    .payload(refreshTokenDTO.getPayload())
                    .scopes(refreshTokenDTO.getScopes())
                    .build();
        }
        this.applicationEventPublisher.publishEvent(new AuthRenewCompletedEvent(this, refreshTokenDTO.getUser()));
        log.info("Refreshed access token for user \"{}\".", refreshTokenDTO.getUser().getId());
        return new AuthTokenKeychain(refreshTokenDTO, accessTokenDTO);
    }

    /**
     * Revokes the specified refresh token, removing it from the repository.
     *
     * @param refreshToken The refresh token to be revoked.
     */
    public void revoke(String refreshToken) {
        User user = this.refreshTokenService.unpack(refreshToken).getUser();
        this.refreshTokenService.findAndDelete(refreshToken);
        this.applicationEventPublisher.publishEvent(new AuthRevokedEvent(this, user));
        log.info("Revoked refresh token for user \"{}\".", user.getId());
    }

    protected AuthenticationContract finalizeAuthentication(Authentication authentication, User user, String[] scopes, ClientInfoDTO clientInfoDTO) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthTokenKeychain authTokenKeychain = new AuthTokenKeychain(
                this.refreshTokenService.generate(user, scopes, null, clientInfoDTO),
                this.accessTokenService.generate(user, scopes, null)
        );
        this.applicationEventPublisher.publishEvent(new AuthCompletedEvent(this, user));
        log.info("Successfully authenticated user \"{}\".", user.getId());
        return new AuthenticationContract(user, authTokenKeychain);
    }
}
