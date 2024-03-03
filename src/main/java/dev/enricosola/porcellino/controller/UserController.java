package dev.enricosola.porcellino.controller;

import dev.enricosola.porcellino.response.user.AuthenticatedSignupResponse;
import dev.enricosola.porcellino.service.AuthenticationService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.enricosola.porcellino.dto.AuthenticationContract;
import org.springframework.web.bind.annotation.PostMapping;
import dev.enricosola.porcellino.form.user.SignupForm;
import dev.enricosola.porcellino.service.UserService;
import dev.enricosola.porcellino.response.Response;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UserController(UserService userService, AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@Valid @ModelAttribute SignupForm signupForm){
        this.userService.createFromForm(signupForm);
        AuthenticationContract authenticationContract = this.authenticationService.authenticateFromForm(signupForm);
        return ResponseEntity.ok().body(new AuthenticatedSignupResponse(authenticationContract));
    }
}
