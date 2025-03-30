package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.repository.UserRepository;
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
     */
    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.getUserByEmail(email);
    }
}
