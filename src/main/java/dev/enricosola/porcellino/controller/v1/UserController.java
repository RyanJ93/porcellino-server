package dev.enricosola.porcellino.controller.v1;

import dev.enricosola.porcellino.dto.TwoFactorAuthRecoveryCodeCollectionDTO;
import dev.enricosola.porcellino.dto.TwoFactorAuthSetupWithQRCodeDTO;
import dev.enricosola.porcellino.dto.user.EnableTwoFactorAuthDTO;
import dev.enricosola.porcellino.response.user.UserInfoResponse;
import dev.enricosola.porcellino.service.AuthenticationService;
import dev.enricosola.porcellino.response.user.SignupResponse;
import dev.enricosola.porcellino.dto.TwoFactorAuthSetupDTO;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.dto.response.user.*;
import dev.enricosola.porcellino.service.UserService;
import dev.enricosola.porcellino.dto.request.user.*;
import dev.enricosola.porcellino.dto.user.UserDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import dev.enricosola.porcellino.entity.User;
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

    /**
     * Set up two-factor authentication.
     */
    @PostMapping("/@me/2fa/setup")
    public ResponseEntity<BaseTwoFactorAuthSetupResponseDTO> setup2FA(
            @RequestParam(required = false, defaultValue = "false") boolean withQRCode,
            Authentication authentication
    ) {
        User user = this.authenticationService.getAuthenticatedUser(authentication);
        BaseTwoFactorAuthSetupResponseDTO baseTwoFactorAuthSetupResponseDTO;
        if (withQRCode) {
            TwoFactorAuthSetupWithQRCodeDTO twoFactorAuthSetupWithQRCodeDTO = this.userService.setup2FAWithQRCode(user.getId());
            baseTwoFactorAuthSetupResponseDTO = TwoFactorAuthSetupWithQRCodeResponseDTO.fromTwoFactorAuthSetupWithQRCodeDTO(twoFactorAuthSetupWithQRCodeDTO);
        } else {
            TwoFactorAuthSetupDTO twoFactorAuthSetupDTO = this.userService.setup2FA(user.getId());
            baseTwoFactorAuthSetupResponseDTO = TwoFactorAuthSetupResponseDTO.fromTwoFactorAuthSetupDTO(twoFactorAuthSetupDTO);
        }
        return ResponseEntity.ok().body(baseTwoFactorAuthSetupResponseDTO);
    }

    /**
     * Enable two-factor authentication.
     */
    @PatchMapping("/@me/2fa/enable")
    public ResponseEntity<EnableTwoFactorAuthResponseDTO> enable2FA(
            @Valid @RequestBody EnableTwoFactorAuthRequestDTO enableTwoFactorAuthRequestDTO,
            Authentication authentication
    ) {
        EnableTwoFactorAuthDTO enableTwoFactorAuthDTO = enableTwoFactorAuthRequestDTO.toServiceDTO();
        User user = this.authenticationService.getAuthenticatedUser(authentication);
        TwoFactorAuthRecoveryCodeCollectionDTO twoFactorAuthRecoveryCodeCollectionDTO = this.userService.enable2FA(user.getId(), enableTwoFactorAuthDTO);
        return ResponseEntity.ok().body(EnableTwoFactorAuthResponseDTO.fromTwoFactorAuthRecoveryCodeCollectionDTO(twoFactorAuthRecoveryCodeCollectionDTO));
    }

    /**
     * Rotates the 2-factor authentication recovery codes for a given user and returns a new set of codes.
     */
    @PostMapping("/@me/2fa/rotate")
    public ResponseEntity<RotateTwoFactorAuthRecoveryCodesResponseDTO> rotate2FARecoveryCodes(
            @Valid @RequestBody RotateTwoFactorAuthRecoveryCodesRequestDTO rotateTwoFactorAuthRecoveryCodesRequestDTO,
            Authentication authentication
    ) {
        User user = this.authenticationService.getAuthenticatedUser(authentication);
        TwoFactorAuthRecoveryCodeCollectionDTO twoFactorAuthRecoveryCodeCollectionDTO = this.userService.rotate2FARecoveryCodes(user.getId(), rotateTwoFactorAuthRecoveryCodesRequestDTO.toServiceDTO());
        return ResponseEntity.ok().body(RotateTwoFactorAuthRecoveryCodesResponseDTO.fromTwoFactorAuthRecoveryCodeCollectionDTO(twoFactorAuthRecoveryCodeCollectionDTO));
    }

    /**
     * Disable two-factor authentication.
     */
    @PatchMapping("/@me/2fa/disable")
    public ResponseEntity<Void> disable2FA(
            @Valid @RequestBody DisableTwoFactorAuthRequestDTO disableTwoFactorAuthRequestDTO,
            Authentication authentication
    ) {
        User user = this.authenticationService.getAuthenticatedUser(authentication);
        this.userService.disable2FA(user.getId(), disableTwoFactorAuthRequestDTO.toServiceDTO());
        return ResponseEntity.noContent().build();
    }

    /**
     * Update user information.
     */
    @PutMapping("/@me")
    public ResponseEntity<UserResponseDTO> update(
            @Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO,
            Authentication authentication
    ) {
        User user = this.authenticationService.getAuthenticatedUser(authentication);
        user = this.userService.findAndUpdate(user.getId(), userUpdateRequestDTO.toServiceDTO());
        return ResponseEntity.ok().body(new UserResponseDTO(user));
    }

    /**
     * Update user password.
     */
    @PatchMapping("/@me/password")
    public ResponseEntity<UserResponseDTO> updatePassword(
            @Valid @RequestBody PasswordUpdateRequestDTO passwordUpdateRequestDTO,
            Authentication authentication
    ) {
        User user = this.authenticationService.getAuthenticatedUser(authentication);
        user = this.userService.findAndUpdatePassword(user.getId(), passwordUpdateRequestDTO.toServiceDTO());
        return ResponseEntity.ok().body(new UserResponseDTO(user));
    }

    /**
     * Initializes the email change process.
     */
    @PostMapping("/@me/email")
    public ResponseEntity<UserResponseDTO> initEmailChange(
            @Valid @RequestBody InitEmailChangeRequestDTO initEmailChangeRequestDTO,
            Authentication authentication
    ) {
        int userId = this.authenticationService.getAuthenticatedUserId(authentication);
        User user = this.userService.findAndInitEmailChange(userId, initEmailChangeRequestDTO.toServiceDTO());
        return ResponseEntity.ok().body(new UserResponseDTO(user));
    }

    /**
     * Complete the email change process.
     */
    @PatchMapping("/@me/email")
    public ResponseEntity<UserResponseDTO> applyEmailChange(
            @Valid @RequestBody ApplyEmailChangeRequestDTO applyEmailChangeRequestDTO,
            Authentication authentication
    ) {
        int userId = this.authenticationService.getAuthenticatedUserId(authentication);
        User user = this.userService.findAndApplyEmailChange(userId, applyEmailChangeRequestDTO.toServiceDTO());
        return ResponseEntity.ok().body(new UserResponseDTO(user));
    }

    /**
     * Abort the email change process.
     */
    @DeleteMapping("/@me/email")
    public ResponseEntity<Void> abortEmailChange(Authentication authentication) {
        int userId = this.authenticationService.getAuthenticatedUserId(authentication);
        this.userService.findAndAbortEmailChange(userId);
        return ResponseEntity.noContent().build();
    }
}
