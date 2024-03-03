package dev.enricosola.porcellino.controller;

import dev.enricosola.porcellino.response.user.AuthenticatedSignupResponse;
import dev.enricosola.porcellino.support.AuthenticationContract;
import dev.enricosola.porcellino.service.AuthenticationService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import dev.enricosola.porcellino.form.user.SignupForm;
import dev.enricosola.porcellino.service.UserService;
import dev.enricosola.porcellino.response.Response;
import org.springframework.http.ResponseEntity;
import dev.enricosola.porcellino.dto.UserDTO;
import org.modelmapper.ModelMapper;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, AuthenticationService authenticationService, ModelMapper modelMapper){
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@Valid @ModelAttribute SignupForm signupForm){
        UserDTO user = this.modelMapper.map(this.userService.createFromForm(signupForm), UserDTO.class);
        AuthenticationContract authenticationContract = this.authenticationService.authenticateFromForm(signupForm);
        return ResponseEntity.ok().body(new AuthenticatedSignupResponse(user, authenticationContract.getToken()));
    }
}
