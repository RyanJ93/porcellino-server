package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.UserNotActiveException;
import dev.enricosola.porcellino.exception.NotFoundException;
import dev.enricosola.porcellino.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import java.util.Optional;

@Service
public class UserLookupService {
    private final UserRepository userRepository;

    public UserLookupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Lookup a user given its email.
     *
     * @param email The email address to lookup.
     *
     * @return The corresponding user.
     *
     * @throws NotFoundException If no user matching given email address is found.
     */
    public User findByEmail(String email) {
        Optional<User> user = this.userRepository.getUserByEmail(email);
        return user.orElseThrow(() -> new NotFoundException("No user found matching email " + email));
    }

    /**
     * Lookup an active user given its email.
     *
     * @param email The email address to lookup.
     *
     * @return The corresponding user.
     *
     * @throws NotFoundException If no user matching the given email address found.
     * @throws UserNotActiveException If user found is not active.
     */
    public User findActiveUserByEmail(String email) {
        User user = this.findByEmail(email);
        if ( !user.isActive() ) {
            throw new UserNotActiveException("User is not active yet.");
        }
        return user;
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
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new NotFoundException("No user found matching ID " + id));
    }
}
