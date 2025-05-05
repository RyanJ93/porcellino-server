package dev.enricosola.porcellino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.enricosola.porcellino.entity.RefreshToken;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    /**
     * Retrieves a list of all refresh tokens associated with a specific user ID.
     *
     * @param userId the ID of the user whose refresh tokens are to be retrieved
     * @return a list of {@code RefreshToken} objects associated with the specified user ID.
     */
    List<RefreshToken> findAllByUserId(int userId);

    /**
     * Retrieves a refresh token based on its associated token value.
     *
     * @param token the token string used to find the associated refresh token
     * @return The refresh token found.
     */
    Optional<RefreshToken> findByToken(String token);
}
