package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.DuplicateEmailAddressException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.support.AuthenticationContract;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.context.ApplicationEventPublisher;
import dev.enricosola.porcellino.repository.UserRepository;
import dev.enricosola.porcellino.events.UserCreatedEvent;
import dev.enricosola.porcellino.dto.user.UserCreateDTO;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthenticationService authenticationService;
    private final UserLookupService userLookupService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(
        ApplicationEventPublisher applicationEventPublisher,
        AuthenticationService authenticationService,
        UserLookupService userLookupService,
        PasswordEncoder passwordEncoder,
        UserRepository userRepository
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.authenticationService = authenticationService;
        this.userLookupService = userLookupService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Lookup a user given its email.
     *
     * @param email The email address to lookup.
     *
     * @return The corresponding user.
     */
    public Optional<User> getUserByEmail(String email) {
        return this.userLookupService.getUserByEmail(email);
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
            this.applicationEventPublisher.publishEvent(new UserCreatedEvent(this, user));
            return user;
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEmailAddressException("Email address already in use.", ex);
        }
    }

    /**
     * Create a new user and then authenticate it.
     *
     * @param userCreateDTO A DTO containing user properties.
     *
     * @return An authentication contract holding both the authentication user and the generated JWT token.
     */
    @Transactional
    public AuthenticationContract createAndAuthenticate(UserCreateDTO userCreateDTO) {
        this.create(userCreateDTO);
        return this.authenticationService.authenticate(userCreateDTO.toUserAuthDTO());
    }
}
