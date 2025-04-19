package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.notifications.user.PasswordResetUserEmailNotification;
import dev.enricosola.porcellino.notifications.user.SignupUserEmailNotification;
import dev.enricosola.porcellino.service.notification.NotificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.context.ApplicationEventPublisher;
import dev.enricosola.porcellino.events.UserActivatedEvent;
import dev.enricosola.porcellino.repository.UserRepository;
import dev.enricosola.porcellino.events.UserCreatedEvent;
import dev.enricosola.porcellino.events.UserUpdatedEvent;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import dev.enricosola.porcellino.exception.*;
import dev.enricosola.porcellino.dto.user.*;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    private final UserVerificationTokenService userVerificationTokenService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NotificationService notificationService;
    private final UserLookupService userLookupService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(
        UserVerificationTokenService userVerificationTokenService,
        ApplicationEventPublisher applicationEventPublisher,
        NotificationService notificationService,
        UserLookupService userLookupService,
        PasswordEncoder passwordEncoder,
        UserRepository userRepository
    ) {
        this.userVerificationTokenService = userVerificationTokenService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.notificationService = notificationService;
        this.userLookupService = userLookupService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Look up a user given its email.
     *
     * @param email The email address to lookup.
     *
     * @return The corresponding user.
     *
     * @throws NotFoundException If no user matching the given email address is found.
     */
    public User findByEmail(String email) {
        return this.userLookupService.findByEmail(email);
    }

    /**
     * Lookup a user given its unique id.
     *
     * @param id The user unique id.
     *
     * @return The corresponding user.
     *
     * @throws NotFoundException If no user matching given ID is found.
     */
    public User find(int id) {
        return this.userLookupService.find(id);
    }

    /**
     * Create a new user.
     *
     * @param userCreateDTO A DTO containing user properties.
     *
     * @return The created user.
     *
     * @throws DuplicateEmailAddressException If provided email address is already in use.
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
            throw new DuplicateEmailAddressException("Email address already in use.", ex);
        }
    }

    /**
     * Activate a given user provided a valid verification token.
     *
     * @param userId The user to activate.
     * @param userActivateDTO a DTO containing the verification token.
     *
     * @return The activated user.
     *
     * @throws VerificationTokenMismatchException If given verification token mismatch.
     */
    @Transactional
    public User findAndActivate(int userId, UserActivateDTO userActivateDTO) {
        User user = this.find(userId);
        if ( !this.userVerificationTokenService.validate(user, userActivateDTO.getToken()) ){
            throw new VerificationTokenMismatchException("Verification token mismatch.");
        }
        user = this.userRepository.save(userActivateDTO.hydrateEntity(user));
        this.applicationEventPublisher.publishEvent(new UserActivatedEvent(this, user));
        this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
        log.info("Activated user \"{}\"", user.getId());
        return user;
    }

    /**
     * Look up a user by email and then send him the activation email message.
     *
     * @param userResendActivationTokenDTO A DTO containing the user email address.
     *
     * @throws UserAlreadyActivatedException If the given user has already been activated.
     * @throws NotFoundException If no user matching the given email address is found.
     */
    public void findAndSendActivationEmail(UserResendActivationTokenDTO userResendActivationTokenDTO) {
        this.sendActivationEmail(this.findByEmail(userResendActivationTokenDTO.getEmail()));
    }

    /**
     * Send the reset password email message to the given user.
     *
     * @param requestPasswordResetDTO A DTO containing the user email address.
     *
     * @throws MalformedVerificationTokenException If the given token is malformed or does not contain the required components.
     * @throws NotFoundException If no user matching the given email address is found.
     * @throws UserNotActiveException If user found has not been activated yet.
     */
    public void requestPasswordReset(RequestPasswordResetDTO requestPasswordResetDTO) {
        User user = this.findByEmail(requestPasswordResetDTO.getEmail());
        if ( !user.isActive() ){
            throw new UserNotActiveException("User has not been activated yet.");
        }
        String token = this.userVerificationTokenService.generate(user);
        this.notificationService.send(new PasswordResetUserEmailNotification(user, token));
        log.info("Password reset email sent to user \"{}\".", user.getId());
    }

    /**
     * Reset a given user password.
     *
     * @param passwordResetDTO A DTO containing the user password and the verification token.
     */
    public void resetPassword(PasswordResetDTO passwordResetDTO) {
        User user = this.userVerificationTokenService.extractUserFromToken(passwordResetDTO.getToken());
        passwordResetDTO.hydrateEntity(user);
        this.userRepository.save(user);
        this.applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));
        log.info("Password reset for user \"{}\".", user.getId());
    }

    /**
     * Send the activation email message to the given user.
     *
     * @param user The user the email will be sent to.
     *
     * @throws UserAlreadyActivatedException If the given user has already been activated.
     */
    private void sendActivationEmail(User user) {
        if ( user.isActive() ) {
            throw new UserAlreadyActivatedException("User already activated.");
        }
        String verificationToken = this.userVerificationTokenService.generate(user);
        this.notificationService.send(new SignupUserEmailNotification(user, verificationToken));
        log.info("Sent activation email to user \"{}\"", user.getId());
    }
}
