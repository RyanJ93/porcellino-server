package dev.enricosola.porcellino.controller.v1;

import dev.enricosola.porcellino.response.user.AuthenticatedSignupResponse;
import dev.enricosola.porcellino.dto.request.user.UserSignupRequestDTO;
import dev.enricosola.porcellino.response.user.UserInfoResponse;
import dev.enricosola.porcellino.support.AuthenticationContract;
import dev.enricosola.porcellino.service.AuthenticationService;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.service.UserService;
import dev.enricosola.porcellino.dto.user.UserDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UserController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * Perform user signup.
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthenticatedSignupResponse> signup(@Valid @RequestBody UserSignupRequestDTO userSignupRequestDTO) {
        AuthenticationContract authenticationContract = this.userService.createAndAuthenticate(userSignupRequestDTO.toServiceDTO());
        UserDTO userDTO = UserDTO.fromEntity(authenticationContract.getUser());
        return ResponseEntity.ok().body(new AuthenticatedSignupResponse(userDTO, authenticationContract.getToken()));
    }

    /**
     * Return authenticated user details.
     */
    @GetMapping("/@me")
    public ResponseEntity<UserInfoResponse> info(Authentication authentication) {
        UserDTO userDTO = UserDTO.fromEntity(this.authenticationService.getAuthenticatedUser(authentication));
        return ResponseEntity.ok().body(new UserInfoResponse(userDTO));
    }
}
