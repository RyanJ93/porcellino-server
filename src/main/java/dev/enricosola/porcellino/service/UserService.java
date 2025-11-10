package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.auth.twofactor.AlreadyEnabledTwoFactorAuthException;
import dev.enricosola.porcellino.exception.auth.twofactor.NotInitializedTwoFactorAuthException;
import dev.enricosola.porcellino.dto.TwoFactorAuthRecoveryCodeCollectionDTO;
import dev.enricosola.porcellino.service.notification.NotificationService;
import dev.enricosola.porcellino.dto.TwoFactorAuthSetupWithQRCodeDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.context.ApplicationEventPublisher;
import dev.enricosola.porcellino.events.UserActivatedEvent;
import dev.enricosola.porcellino.dto.TwoFactorAuthSetupDTO;
import dev.enricosola.porcellino.repository.UserRepository;
import dev.enricosola.porcellino.events.UserCreatedEvent;
import dev.enricosola.porcellino.events.UserUpdatedEvent;
import dev.enricosola.porcellino.notifications.user.*;
import dev.enricosola.porcellino.exception.user.*;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import dev.enricosola.porcellino.dto.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final UserVerificationTokenService userVerificationTokenService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TwoFactorAuthService twoFactorAuthService;
    private final NotificationService notificationService;
    private final UserLookupService userLookupService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Look up a user given its email.
     *
     * @param email The email address to lookup.
     * @return The corresponding user.
     * @throws NotFoundUserException If no user matching the given email address is found.
     */
    public User findByEmail(String email) {
        return this.userLookupService.findByEmail(email);
    }

    /**
     * Lookup a user given its unique id.
     *
     * @param id The user unique id.
     * @return The corresponding user.
     * @throws NotFoundUserException If no user matching the given email address is found.
     */
    public User find(int id) {
        return this.userLookupService.find(id);
    }

    /**
     * Create a new user.
     *
     * @param userCreateDTO A DTO containing user properties.
     * @return The created user.
     * @throws DuplicateEmailAddressUserException If provided email address is already in use.
     * @throws NotCreatedUserException If the user could not be created.
     */
    @Transactional
    public User create(UserCreateDTO userCreateDTO) {
        try {
            User user = userCreateDTO.toEntity();
            user.setPassword(this.passwordEncoder.encode(userCreateDTO.getPassword()));
            user = this.userRepository.saveAndFlush(user);
            this.sendActivationEmail(user);
            this.applicationEventPublisher.publishEvent(new UserCreatedEvent(this, user));
            log.info("Created new user with ID \"{}\"", user.getId());
            return user;
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEmailAddressUserException("Email address already in use.", ex);
        } catch (Exception ex) {
            throw new NotCreatedUserException("Could not create user.", ex);
        }
    }

    /**
     * Activate a given user provided a valid verification token.
     *
     * @param userId The user to activate.
     * @param userActivateDTO a DTO containing the verification token.
     * @return The activated user.
     * @throws VerificationTokenMismatchUserException If given verification token mismatch.
     * @throws NotFoundUserException If no user matching the given email address is found.
     * @throws NotActivatedUserException If the user could not be activated.
     */
    @Transactional
    public User findAndActivate(int userId, UserActivateDTO userActivateDTO) {
        User user = this.find(userId);
        if ( !this.userVerificationTokenService.validate(user, userActivateDTO.getToken()) ){
            throw new VerificationTokenMismatchUserException("Verification token mismatch.");
        }
        try {
            user = this.userRepository.save(userActivateDTO.hydrateEntity(user));
            this.applicationEventPublisher.publishEvent(new UserActivatedEvent(this, user));
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            log.info("Activated user \"{}\"", user.getId());
            return user;
        } catch (Exception ex) {
            throw new NotActivatedUserException("Could not activate user.", ex);
        }
    }

    /**
     * Look up a user by email and then send him the activation email message.
     *
     * @param userResendActivationTokenDTO A DTO containing the user email address.
     * @throws AlreadyActivatedUserException If the given user has already been activated.
     * @throws NotFoundUserException If no user matching the given email address is found.
     */
    public void findAndSendActivationEmail(UserResendActivationTokenDTO userResendActivationTokenDTO) {
        this.sendActivationEmail(this.findByEmail(userResendActivationTokenDTO.getEmail()));
    }

    /**
     * Finds a user by the provided ID and then updates the user's information.
     *
     * @param id the ID of the user to be updated.
     * @param userUpdateDTO an object containing the updated information for the user.
     * @return the updated user object after saving to the repository.
     * @throws NotFoundUserException If no user matching given ID is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    public User findAndUpdate(int id, UserUpdateDTO userUpdateDTO) {
        User user = this.userLookupService.find(id);
        try {
            user = userUpdateDTO.hydrateEntity(user);
            this.userRepository.save(user);
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            return user;
        } catch (Exception ex) {
            throw new NotUpdatedUserException("Could not update user.", ex);
        }
    }

    /**
     * Finds a user by the given ID, verifies the old password, updates the password
     * to the new one, and persists the updated user information to the repository.
     * An event is published upon a successful update.
     *
     * @param id The ID of the user whose password is to be updated.
     * @param passwordUpdateDTO The DTO containing the old password for verification and the new password for updating the user's credentials.
     * @return The updated User object with the new password saved.
     * @throws PasswordMismatchUserException If the provided old password does not match the current password.
     * @throws NotFoundUserException If no user matching given ID is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    public User findAndUpdatePassword(int id, PasswordUpdateDTO passwordUpdateDTO) {
        User user = this.userLookupService.find(id);
        if ( !this.passwordEncoder.matches(passwordUpdateDTO.getOldPassword(), user.getPassword()) ){
            throw new PasswordMismatchUserException("Old password does not match current password.");
        }
        try {
            user.setPassword(this.passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
            this.userRepository.save(user);
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            return user;
        } catch (Exception ex) {
            throw new NotUpdatedUserException("Could not update user.", ex);
        }
    }

    /**
     * Finds a user by their ID, initializes their email change process, and sends a confirmation email with a verification token.
     *
     * @param id The unique identifier of the user.
     * @param initEmailChangeDTO The DTO containing information to initialize the email change process for the user.
     * @return The updated User object after the email change initialization.
     * @throws InvalidPendingEmailUserException If the user does not have a valid pending email address.
     * @throws InvalidNewEmailAddressUserException If given address is the same as the current one.
     * @throws InvalidNewEmailAddressUserException If an invalid email address is provided.
     * @throws NotFoundUserException If no user matching given ID is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    @Transactional
    public User findAndInitEmailChange(int id, InitEmailChangeDTO initEmailChangeDTO) {
        User user = this.userLookupService.find(id);
        if (initEmailChangeDTO == null || initEmailChangeDTO.getEmail() == null || initEmailChangeDTO.getEmail().isBlank()) {
            throw new InvalidNewEmailAddressUserException("Invalid email address provided.");
        }
        if (user.getEmail().equals(initEmailChangeDTO.getEmail())) {
            throw new InvalidNewEmailAddressUserException("New email address cannot be the same as the current one.");
        }
        try {
            initEmailChangeDTO.hydrateEntity(user);
            String verificationToken = this.emailVerificationTokenService.generate(user);
            user = this.userRepository.save(user);
            this.notificationService.send(new ConfirmEmailChangeUserEmailNotification(user, verificationToken));
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            return user;
        } catch (Exception ex) {
            throw new NotUpdatedUserException("Could not update user.", ex);
        }
    }

    /**
     * Finds a user based on a pending email extracted from the given verification token and applies the email change to the user's account.
     *
     * @param id the ID of the user requesting the email change.
     * @param applyEmailChangeDTO the data transfer object containing the email change information, including the verification token.
     * @return the updated User object after the email change has been applied.
     * @throws MismatchUserException if the given user ID does not match the user associated with the provided token.
     * @throws NotFoundUserException If no user matching the given pending email address is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    public User findAndApplyEmailChange(int id, ApplyEmailChangeDTO applyEmailChangeDTO) {
        String pendingEmail = this.emailVerificationTokenService.extractPendingEmailFromToken(applyEmailChangeDTO.getToken());
        User user = this.userLookupService.findByPendingEmail(pendingEmail);
        if ( id != user.getId() ) {
            throw new MismatchUserException("Given user ID does not match the user associated with the given verification token.");
        }
        try {
            user.setEmail(pendingEmail);
            user.setPendingEmail(null);
            this.userRepository.save(user);
            this.notificationService.send(new PreviousEmailChangeUserEmailNotification(user, pendingEmail));
            this.notificationService.send(new EmailChangeUserEmailNotification(user));
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            return user;
        } catch (Exception ex) {
            throw new NotUpdatedUserException("Could not update user.", ex);
        }
    }

    /**
     * Finds a user by their ID and aborts the pending email change by setting the pending email to null.
     *
     * @param id the unique identifier of the user.
     * @return the updated User object with the pending email change aborted.
     * @throws NotFoundUserException If no user matching given ID is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    public User findAndAbortEmailChange(int id) {
        User user = this.userLookupService.find(id);
        try {
            user.setPendingEmail(null);
            this.userRepository.save(user);
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            return user;
        } catch (Exception ex) {
            throw new NotUpdatedUserException("Could not update user.", ex);
        }
    }

    /**
     * Send the reset password email message to the given user.
     *
     * @param requestPasswordResetDTO A DTO containing the user email address.
     *
     * @throws MalformedVerificationTokenUserException If the given token is malformed or does not contain the required components.
     * @throws NotActiveUserException If user found has not been activated yet.
     * @throws NotFoundUserException If no user matching given ID is found.
     */
    public void requestPasswordReset(RequestPasswordResetDTO requestPasswordResetDTO) {
        User user = this.findByEmail(requestPasswordResetDTO.getEmail());
        if ( !user.isActive() ){
            throw new NotActiveUserException("User has not been activated yet.");
        }
        String token = this.userVerificationTokenService.generate(user);
        this.notificationService.send(new PasswordResetUserEmailNotification(user, token));
        log.info("Password reset email sent to user \"{}\".", user.getId());
    }

    /**
     * Reset a given user password.
     *
     * @param passwordResetDTO A DTO containing the user password and the verification token.
     * @throws NotFoundUserException If no user matching given ID is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    public void resetPassword(PasswordResetDTO passwordResetDTO) {
        User user = this.userVerificationTokenService.extractUserFromToken(passwordResetDTO.getToken());
        try {
            passwordResetDTO.hydrateEntity(user);
            this.userRepository.save(user);
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            log.info("Password reset for user \"{}\".", user.getId());
        } catch (Exception ex) {
            throw new NotUpdatedUserException("Could not update user.", ex);
        }
    }

    /**
     * Set up two-factor authentication (2FA) for a specific user.
     *
     * @param userId ID of the user to set up 2FA for
     * @return TwoFactorAuthSetupDTO containing the secret required by the client to set up the 2FA and a URL which can be used to display a QR code that clients can scan.
     * @throws NotFoundUserException If no user matching given ID is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    public TwoFactorAuthSetupDTO setup2FA(int userId) {
        User user = this.userLookupService.find(userId);
        try {
            TwoFactorAuthSetupDTO twoFactorAuthSetupDTO = this.twoFactorAuthService.setup(user.getEmail());
            user.setTwoFactorAuthSecret(twoFactorAuthSetupDTO.getSecret());
            this.userRepository.save(user);
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            log.info("2FA setup for user \"{}\".", user.getId());
            return twoFactorAuthSetupDTO;
        } catch (Exception ex) {
            throw new NotUpdatedUserException("Could not update user.", ex);
        }
    }

    /**
     * Sets up two-factor authentication with a QR code for the specified user and returns the setup details, including the QR code image URL.
     *
     * @param userId The unique identifier of the user whose account needs to be set up for two-factor authentication.
     * @return A {@link TwoFactorAuthSetupWithQRCodeDTO} object containing the setup details, including the QR code image URL.
     * @throws NotFoundUserException If no user matching given ID is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    public TwoFactorAuthSetupWithQRCodeDTO setup2FAWithQRCode(int userId) {
        return this.twoFactorAuthService.injectQRCode(this.setup2FA(userId));
    }

    /**
     * Enables two-factor authentication (2FA) for the specified user.
     *
     * @param userId The unique identifier of the user for whom 2FA is being enabled.
     * @param enableTwoFactorAuthDTO An object containing the required data, including the 2FA code, to enable 2FA.
     * @return A collection of recovery codes (encapsulated within a TwoFactorAuthRecoveryCodeCollectionDTO)
     *         for the user, which can be used to recover access in case the user loses their 2FA device.
     * @throws NotFoundUserException If no user matching given ID is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    @Transactional
    public TwoFactorAuthRecoveryCodeCollectionDTO enable2FA(int userId, EnableTwoFactorAuthDTO enableTwoFactorAuthDTO) {
        User user = this.userLookupService.find(userId);
        if ( user.is2FAEnabled() ){
            throw new AlreadyEnabledTwoFactorAuthException("2FA already enabled.");
        }
        String secret = user.getTwoFactorAuthSecret(), code = enableTwoFactorAuthDTO.getCode();
        var twoFactorAuthConfigurationDTO = this.twoFactorAuthService.checkAndEnable(userId, secret, code);
        try {
            user.setTwoFactorAuthEnabledAt(new Date());
            this.userRepository.save(user);
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            log.info("2FA enabled for user \"{}\".", user.getId());
            return twoFactorAuthConfigurationDTO;
        } catch (Exception ex) {
            throw new NotUpdatedUserException("Could not update user.", ex);
        }
    }

    /**
     * Rotates the two-factor authentication recovery codes for a user.
     *
     * @param userId The unique identifier of the user whose recovery codes are to be rotated.
     * @return A TwoFactorAuthRecoveryCodeCollectionDTO containing the newly generated recovery codes.
     * @throws NotInitializedTwoFactorAuthException If two-factor authentication has not been initialized for the user.
     */
    public TwoFactorAuthRecoveryCodeCollectionDTO rotate2FARecoveryCodes(int userId, RotateTwoFactorAuthRecoveryCodesDTO rotateTwoFactorAuthRecoveryCodesDTO) {
        User user = this.userLookupService.find(userId);
        if ( user.getTwoFactorAuthEnabledAt() == null ){
            throw new NotInitializedTwoFactorAuthException("Two-factor authentication has not been initialized yet.");
        }
        this.twoFactorAuthService.check(user.getTwoFactorAuthSecret(), rotateTwoFactorAuthRecoveryCodesDTO.getCode());
        log.info("Rotating recovery codes for user \"{}\".", user.getId());
        return this.twoFactorAuthService.rotateRecoveryCodes(userId);
    }

    /**
     * Disables two-factor authentication (2FA) for the specified user.
     *
     * @param userId The unique identifier of the user for whom 2FA is to be disabled.
     * @param disableTwoFactorAuthDTO An object containing data required to validate and perform the 2FA disabling process.
     * @throws NotFoundUserException If no user matching given ID is found.
     * @throws NotUpdatedUserException If the user could not be updated.
     */
    @Transactional
    public void disable2FA(int userId, DisableTwoFactorAuthDTO disableTwoFactorAuthDTO) {
        User user = this.userLookupService.find(userId);
        String secret = user.getTwoFactorAuthSecret(), code = disableTwoFactorAuthDTO.getCode();
        this.twoFactorAuthService.checkAndDisable(userId, secret, code);
        try {
            user.setTwoFactorAuthEnabledAt(null);
            user.setTwoFactorAuthSecret(null);
            this.userRepository.save(user);
            this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
            log.info("2FA disabled for user \"{}\".", user.getId());
        } catch (Exception ex) {
            throw new NotUpdatedUserException("Could not update user.", ex);
        }
    }

    /**
     * Send the activation email message to the given user.
     *
     * @param user The user the email will be sent to.
     * @throws AlreadyActivatedUserException If the given user has already been activated.
     */
    private void sendActivationEmail(User user) {
        if ( user.isActive() ) {
            throw new AlreadyActivatedUserException("User already activated.");
        }
        String verificationToken = this.userVerificationTokenService.generate(user);
        this.notificationService.send(new SignupUserEmailNotification(user, verificationToken));
        log.info("Sent activation email to user \"{}\"", user.getId());
    }
}
