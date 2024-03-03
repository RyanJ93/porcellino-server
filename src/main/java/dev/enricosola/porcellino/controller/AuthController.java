package dev.enricosola.porcellino.controller;

import dev.enricosola.porcellino.dto.AuthenticationContract;
import dev.enricosola.porcellino.service.AuthenticationService;
import dev.enricosola.porcellino.response.auth.LoginResponse;
import dev.enricosola.porcellino.form.auth.LoginForm;
import dev.enricosola.porcellino.response.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @ModelAttribute LoginForm loginForm){
        AuthenticationContract authenticationContract = this.authenticationService.authenticateFromForm(loginForm);
        return ResponseEntity.ok().body(new LoginResponse(authenticationContract));
    }
}
