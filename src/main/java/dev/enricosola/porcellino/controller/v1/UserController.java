package dev.enricosola.porcellino.controller.v1;

import dev.enricosola.porcellino.response.user.UserInfoResponse;
import dev.enricosola.porcellino.service.AuthenticationService;
import dev.enricosola.porcellino.response.user.SignupResponse;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.service.UserService;
import dev.enricosola.porcellino.dto.request.user.*;
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
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody UserSignupRequestDTO userSignupRequestDTO) {
        UserDTO userDTO = UserDTO.fromEntity(this.userService.create(userSignupRequestDTO.toServiceDTO()));
        return ResponseEntity.ok().body(new SignupResponse(userDTO));
    }

    /**
     * Return authenticated user details.
     */
    @GetMapping("/@me")
    public ResponseEntity<UserInfoResponse> me(Authentication authentication) {
        UserDTO userDTO = UserDTO.fromEntity(this.authenticationService.getAuthenticatedUser(authentication));
        return ResponseEntity.ok().body(new UserInfoResponse(userDTO));
    }

    /**
     * Activate a given user provided a valid verification token.
     */
    @PatchMapping("/{userId}/activate")
    public ResponseEntity<UserInfoResponse> activate(@PathVariable int userId, @Valid @RequestBody UserActivateRequestDTO userActivateRequestDTO) {
        UserDTO userDTO = UserDTO.fromEntity(this.userService.findAndActivate(userId, userActivateRequestDTO.toServiceDTO()));
        return ResponseEntity.ok().body(new UserInfoResponse(userDTO));
    }

    /**
     * Resend verification email for a given user.
     */
    @PostMapping("/@me/resend-activation-token")
    public ResponseEntity<Void> resendActivationToken(@Valid @RequestBody UserResendActivationTokenRequestDTO userResendActivationTokenRequestDTO) {
        this.userService.findAndSendActivationEmail(userResendActivationTokenRequestDTO.toServiceDTO());
        return ResponseEntity.noContent().build();
    }

    /**
     * Send a password-reset email to the user matching a given email.
     */
    @PostMapping("/@me/request-password-reset")
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody RequestPasswordResetRequestDTO requestPasswordResetRequestDTO) {
        this.userService.requestPasswordReset(requestPasswordResetRequestDTO.toServiceDTO());
        return ResponseEntity.noContent().build();
    }

    /**
     * Reset the user's password using a given password reset token.
     */
    @PatchMapping("/@me/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetRequestDTO passwordResetRequestDTO) {
        this.userService.resetPassword(passwordResetRequestDTO.toServiceDTO());
        return ResponseEntity.noContent().build();
    }
}
