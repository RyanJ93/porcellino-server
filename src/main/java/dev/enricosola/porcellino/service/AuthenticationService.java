package dev.enricosola.porcellino.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import dev.enricosola.porcellino.form.auth.CredentialsAwareForm;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.util.JwtUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtils jwtUtils){
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public String authenticateFromForm(CredentialsAwareForm credentialsAwareForm){
        return this.authenticate(credentialsAwareForm.getEmail(), credentialsAwareForm.getPassword());
    }

    public String authenticate(String email, String password){
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return this.jwtUtils.generateJwtToken(authentication);
    }
}
