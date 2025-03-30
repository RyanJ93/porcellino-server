package dev.enricosola.porcellino.controller.v1;

import dev.enricosola.porcellino.dto.request.user.UserAuthRequestDTO;
import dev.enricosola.porcellino.support.AuthenticationContract;
import dev.enricosola.porcellino.service.AuthenticationService;
import dev.enricosola.porcellino.response.auth.LoginResponse;
import dev.enricosola.porcellino.response.auth.RenewResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import dev.enricosola.porcellino.dto.user.UserDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Perform user authentication.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserAuthRequestDTO userAuthRequestDTO){
        AuthenticationContract authenticationContract = this.authenticationService.authenticate(userAuthRequestDTO.toServiceDTO());
        UserDTO userDTO = UserDTO.fromEntity(authenticationContract.getUser());
        return ResponseEntity.ok().body(new LoginResponse(userDTO, authenticationContract.getToken()));
    }

    /**
     * Renew JWT token being used.
     */
    @GetMapping("/renew")
    public ResponseEntity<RenewResponse> renew(Authentication authentication){
        String token = this.authenticationService.renew(authentication);
        return ResponseEntity.ok().body(new RenewResponse(token));
    }
}
