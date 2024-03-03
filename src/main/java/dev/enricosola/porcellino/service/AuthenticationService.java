package dev.enricosola.porcellino.service;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import dev.enricosola.porcellino.form.auth.CredentialsAwareForm;
import dev.enricosola.porcellino.support.AuthenticationContract;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.util.JwtUtils;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;

@Service
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
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = this.jwtUtils.generateJwtToken(authentication);
        return new AuthenticationContract(token, user);
    }
}
