package dev.enricosola.porcellino.controller;

import dev.enricosola.porcellino.response.user.AuthenticatedSignupResponse;
import dev.enricosola.porcellino.response.BindingErrorResponse;
import dev.enricosola.porcellino.service.AuthenticationService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import dev.enricosola.porcellino.form.user.SignupForm;
import dev.enricosola.porcellino.service.UserService;
import org.springframework.validation.BindingResult;
import dev.enricosola.porcellino.response.Response;
import org.springframework.http.ResponseEntity;
import dev.enricosola.porcellino.entity.User;
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
    public ResponseEntity<Response> signup(@Valid @ModelAttribute SignupForm signupForm, BindingResult bindingResult){
        if ( bindingResult.hasErrors() ){
            return ResponseEntity.badRequest().body(new BindingErrorResponse(bindingResult));
        }
        User user = this.userService.createFromForm(signupForm);
        String jwt = this.authenticationService.authenticateFromForm(signupForm);
        return ResponseEntity.ok().body(new AuthenticatedSignupResponse(user, jwt));
    }
}
