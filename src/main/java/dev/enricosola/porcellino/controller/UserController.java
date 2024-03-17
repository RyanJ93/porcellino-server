package dev.enricosola.porcellino.controller;

import dev.enricosola.porcellino.response.user.AuthenticatedSignupResponse;
import dev.enricosola.porcellino.response.user.UserInfoResponse;
import dev.enricosola.porcellino.support.AuthenticationContract;
import dev.enricosola.porcellino.service.AuthenticationService;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.form.user.SignupForm;
import dev.enricosola.porcellino.service.UserService;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<AuthenticatedSignupResponse> signup(@Valid @ModelAttribute SignupForm signupForm){
        UserDTO user = this.modelMapper.map(this.userService.createFromForm(signupForm), UserDTO.class);
        AuthenticationContract authenticationContract = this.authenticationService.authenticateFromForm(signupForm);
        return ResponseEntity.ok().body(new AuthenticatedSignupResponse(user, authenticationContract.getToken()));
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> info(Authentication authentication){
        UserDTO user = this.modelMapper.map(this.authenticationService.getAuthenticatedUser(authentication), UserDTO.class);
        return ResponseEntity.ok().body(new UserInfoResponse(user));
    }
}
