package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.user.NotActiveUserException;
import dev.enricosola.porcellino.exception.user.NotFoundUserException;
import dev.enricosola.porcellino.repository.UserRepository;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLookupService {
    private final UserRepository userRepository;

    /**
     * Look up a user given its email.
     *
     * @param email The email address to lookup.
     *
     * @return The corresponding user.
     *
     * @throws NotFoundUserException If no user matching the given email address is found.
     */
    public User findByEmail(String email) {
        return this.userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("No user found matching email " + email));
    }

    /**
     * Look up a user by their pending email address.
     *
     * @param pendingEmail The email address to look up that is marked as pending verification.
     *
     * @return The corresponding user whose pending email matches the input.
     *
     * @throws NotFoundUserException If no user matching the given pending email address is found.
     */
    public User findByPendingEmail(String pendingEmail) {
        return this.userRepository.getUserByPendingEmail(pendingEmail)
                .orElseThrow(() -> new NotFoundUserException("No user found matching email " + pendingEmail));
    }

    /**
     * Look up an active user given its email.
     *
     * @param email The email address to lookup.
     *
     * @return The corresponding user.
     *
     * @throws NotFoundUserException If no user matching the given email address found.
     * @throws NotActiveUserException If the user found is not active.
     */
    public User findActiveUserByEmail(String email) {
        User user = this.findByEmail(email);
        if ( !user.isActive() ) {
            throw new NotActiveUserException("User is not active yet.");
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
     * @throws NotFoundUserException If no user matching given ID is found.
     */
    public User find(int id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("No user found matching ID " + id));
    }
}
