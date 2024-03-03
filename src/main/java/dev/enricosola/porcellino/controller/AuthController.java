package dev.enricosola.porcellino.controller;

import dev.enricosola.porcellino.support.AuthenticationContract;
import dev.enricosola.porcellino.service.AuthenticationService;
import dev.enricosola.porcellino.response.auth.LoginResponse;
import dev.enricosola.porcellino.form.auth.LoginForm;
import dev.enricosola.porcellino.response.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import dev.enricosola.porcellino.dto.UserDTO;
import org.modelmapper.ModelMapper;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    public AuthController(AuthenticationService authenticationService, ModelMapper modelMapper){
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @ModelAttribute LoginForm loginForm){
        AuthenticationContract authenticationContract = this.authenticationService.authenticateFromForm(loginForm);
        UserDTO user = this.modelMapper.map(authenticationContract.getUser(), UserDTO.class);
        return ResponseEntity.ok().body(new LoginResponse(user, authenticationContract.getToken()));
    }
}
