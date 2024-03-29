package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.support.AuthenticatedUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import dev.enricosola.porcellino.form.auth.CredentialsAwareForm;
import dev.enricosola.porcellino.support.AuthenticationContract;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.util.JwtUtils;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    public AuthenticationContract authenticateFromForm(CredentialsAwareForm credentialsAwareForm){
        return this.authenticate(credentialsAwareForm.getEmail(), credentialsAwareForm.getPassword());
    }

    public AuthenticationContract authenticate(String email, String password){
        User user = this.userService.getUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user matching the given email address found."));
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        log.info("Successfully authenticated user \"" + email + "\".");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = this.jwtUtils.generateJwtToken(authentication);
        return new AuthenticationContract(token, user);
    }

    public User getAuthenticatedUser(Authentication authentication){
        AuthenticatedUserDetails authenticatedUserDetails = (AuthenticatedUserDetails)authentication.getPrincipal();
        return this.userService.getUserByEmail(authenticatedUserDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    public String renew(Authentication authentication){
        return this.jwtUtils.generateJwtToken(authentication);
    }
}
