package dev.enricosola.porcellino.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import dev.enricosola.porcellino.support.AuthenticatedUserDetails;
import dev.enricosola.porcellino.exception.UserNotActiveException;
import dev.enricosola.porcellino.support.AuthenticationContract;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.events.UserAuthenticatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import dev.enricosola.porcellino.exception.NotFoundException;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.dto.user.UserAuthDTO;
import dev.enricosola.porcellino.util.JwtUtils;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthenticationManager authenticationManager;
    private final UserLookupService userLookupService;
    private final JwtUtils jwtUtils;

    public AuthenticationService(
        ApplicationEventPublisher applicationEventPublisher,
        AuthenticationManager authenticationManager,
        UserLookupService userLookupService,
        JwtUtils jwtUtils
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.authenticationManager = authenticationManager;
        this.userLookupService = userLookupService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Perform user authentication.
     *
     * @param userAuthDTO The user credentials.
     *
     * @return An authentication contract holding both the authentication user and the generated JWT token.
     *
     * @throws UsernameNotFoundException If no user matching the given email address found.
     * @throws UserNotActiveException If user found is not active.
     */
    @Transactional
    public AuthenticationContract authenticate(UserAuthDTO userAuthDTO) {
        try {
            String email = userAuthDTO.getEmail(), password = userAuthDTO.getPassword();
            User user = this.userLookupService.findActiveUserByEmail(email);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = this.jwtUtils.generateJwtToken(authentication);

            this.applicationEventPublisher.publishEvent(new UserAuthenticatedEvent(this, user));
            log.info("Successfully authenticated user \"{}\".", user.getId());
            return new AuthenticationContract(token, user);
        } catch (NotFoundException ex) {
            throw new UsernameNotFoundException("No user matching the given email address found.");
        }
    }

    /**
     * Return currently authenticated user.
     *
     * @param authentication The authentication.
     *
     * @return The authenticated user found.
     *
     * @throws UsernameNotFoundException If no valid authenticated user is found.
     */
    public User getAuthenticatedUser(Authentication authentication) {
        try {
            AuthenticatedUserDetails authenticatedUserDetails = (AuthenticatedUserDetails)authentication.getPrincipal();
            return this.userLookupService.findByEmail(authenticatedUserDetails.getUsername());
        } catch (NotFoundException ex) {
            throw new UsernameNotFoundException("User not found.", ex);
        }
    }

    /**
     * Renew JWT token being used.
     *
     * @param authentication The authentication.
     *
     * @return The generated JWT token.
     */
    public String renew(Authentication authentication) {
        return this.jwtUtils.generateJwtToken(authentication);
    }
}
