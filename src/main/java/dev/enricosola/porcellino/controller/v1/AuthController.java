package dev.enricosola.porcellino.controller.v1;

import dev.enricosola.porcellino.dto.request.auth.TwoFactorRecoveryCodeChallengeRequestDTO;
import dev.enricosola.porcellino.dto.request.auth.TwoFactorCodeChallengeRequestDTO;
import dev.enricosola.porcellino.dto.request.auth.AccessTokenRefreshRequestDTO;
import dev.enricosola.porcellino.dto.response.auth.TokenRefreshResponseDTO;
import dev.enricosola.porcellino.dto.request.user.UserAuthRequestDTO;
import dev.enricosola.porcellino.dto.response.auth.LoginResponseDTO;
import dev.enricosola.porcellino.support.AuthenticationContract;
import dev.enricosola.porcellino.service.AuthenticationService;
import org.springframework.web.server.ResponseStatusException;
import dev.enricosola.porcellino.support.AuthTokenKeychain;
import dev.enricosola.porcellino.facades.UserTokenStorage;
import dev.enricosola.porcellino.dto.UserTokenResponseDTO;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.dto.user.UserAuthDTO;
import dev.enricosola.porcellino.dto.ClientInfoDTO;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Perform user authentication.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestParam(required = false, defaultValue = "false") boolean useCookies,
            @Valid @RequestBody UserAuthRequestDTO userAuthRequestDTO,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest
    ) {
        UserAuthDTO userAuthDTO = userAuthRequestDTO.toServiceDTO(ClientInfoDTO.buildFromHttpRequest(httpServletRequest));
        AuthenticationContract authenticationContract = this.authenticationService.authenticate(userAuthDTO);
        AuthTokenKeychain authTokenKeychain = authenticationContract.getAuthTokenKeychain();
        if (useCookies) {
            UserTokenStorage.attachToResponse(httpServletResponse, authTokenKeychain);
        }
        return ResponseEntity.ok().body(new LoginResponseDTO(
                UserTokenResponseDTO.fromUserTokenDTO(authTokenKeychain.getAccessToken()),
                UserTokenResponseDTO.fromUserTokenDTO(authTokenKeychain.getRefreshToken())
        ));
    }

    /**
     * Validate the provided two-factor authentication code and generate new access and refresh tokens if successful.
     */
    @PostMapping("/2fa/challenge")
    public ResponseEntity<LoginResponseDTO> challenge2FA(
            @Valid @RequestBody TwoFactorCodeChallengeRequestDTO twoFactorChallengeRequestDTO,
            @RequestParam(required = false, defaultValue = "false") boolean useCookies,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest,
            Authentication authentication
    ) {
        ClientInfoDTO clientInfoDTO = ClientInfoDTO.buildFromHttpRequest(httpServletRequest);
        AuthenticationContract authenticationContract = this.authenticationService.challenge2FACode(
                this.authenticationService.getAuthenticatedUser(authentication).getId(),
                twoFactorChallengeRequestDTO.toServiceDTO(clientInfoDTO)
        );
        AuthTokenKeychain authTokenKeychain = authenticationContract.getAuthTokenKeychain();
        if (useCookies) {
            UserTokenStorage.attachToResponse(httpServletResponse, authTokenKeychain);
        }
        return ResponseEntity.ok().body(new LoginResponseDTO(
                UserTokenResponseDTO.fromUserTokenDTO(authTokenKeychain.getAccessToken()),
                UserTokenResponseDTO.fromUserTokenDTO(authTokenKeychain.getRefreshToken())
        ));
    }

    /**
     * Validate the provided two-factor recovery code and generate new access and refresh tokens if successful.
     */
    @PostMapping("/2fa/challenge-recovery")
    public ResponseEntity<LoginResponseDTO> challenge2FARecovery(
            @Valid @RequestBody TwoFactorRecoveryCodeChallengeRequestDTO twoFactorRecoveryCodeChallengeRequestDTO,
            @RequestParam(required = false, defaultValue = "false") boolean useCookies,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest,
            Authentication authentication
    ) {
        ClientInfoDTO clientInfoDTO = ClientInfoDTO.buildFromHttpRequest(httpServletRequest);
        AuthenticationContract authenticationContract = this.authenticationService.challenge2FARecoveryCode(
                this.authenticationService.getAuthenticatedUser(authentication).getId(),
                twoFactorRecoveryCodeChallengeRequestDTO.toServiceDTO(clientInfoDTO)
        );
        AuthTokenKeychain authTokenKeychain = authenticationContract.getAuthTokenKeychain();
        if (useCookies) {
            UserTokenStorage.attachToResponse(httpServletResponse, authTokenKeychain);
        }
        return ResponseEntity.ok().body(new LoginResponseDTO(
                UserTokenResponseDTO.fromUserTokenDTO(authTokenKeychain.getAccessToken()),
                UserTokenResponseDTO.fromUserTokenDTO(authTokenKeychain.getRefreshToken())
        ));
    }

    /**
     * Renew JWT token being used.
     */
    @PatchMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDTO> refresh(
            @Valid @RequestBody AccessTokenRefreshRequestDTO accessTokenRefreshRequestDTO,
            @RequestParam(required = false, defaultValue = "false") boolean useCookies,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest
    ) {
        String refreshToken = Optional.ofNullable(accessTokenRefreshRequestDTO.getRefreshToken())
                .orElse(UserTokenStorage.getRefreshTokenFromRequest(httpServletRequest));
        if ( refreshToken == null || refreshToken.isBlank() ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token.");
        }
        AuthTokenKeychain authTokenKeychain = this.authenticationService.refreshAccessToken(refreshToken, true);
        if (useCookies) {
            UserTokenStorage.attachToResponse(httpServletResponse, authTokenKeychain);
        }
        return ResponseEntity.ok().body(new TokenRefreshResponseDTO(
                UserTokenResponseDTO.fromUserTokenDTO(authTokenKeychain.getAccessToken()),
                UserTokenResponseDTO.fromUserTokenDTO(authTokenKeychain.getRefreshToken())
        ));
    }

    /**
     * Perform user logout, revoking the provided or extracted refresh token and removing it from cookies if required.
     */
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody AccessTokenRefreshRequestDTO accessTokenRefreshRequestDTO,
            @RequestParam(required = false, defaultValue = "false") boolean useCookies,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest
    ) {
        String refreshToken = Optional.ofNullable(accessTokenRefreshRequestDTO.getRefreshToken())
                .orElse(UserTokenStorage.getRefreshTokenFromRequest(httpServletRequest));
        if ( refreshToken == null || refreshToken.isBlank() ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token.");
        }
        this.authenticationService.revoke(refreshToken);
        if (useCookies) {
            UserTokenStorage.dropFromResponse(httpServletResponse);
        }
        return ResponseEntity.noContent().build();
    }
}
