package dev.enricosola.porcellino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import dev.enricosola.porcellino.entity.RecoveryCode;
import java.util.List;

public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, Integer> {
    /**
     * Retrieves a list of all recovery codes associated with a specific user ID, filtering out any that have been invalidated.
     *
     * @param userId the ID of the user whose active recovery codes are to be retrieved
     * @return a list of RecoveryCode entities associated with the specified user ID that have not been invalidated
     */
    @Query("SELECT rc FROM RecoveryCode rc WHERE rc.userId = :userId AND rc.invalidatedAt IS NULL")
    List<RecoveryCode> findAllByUserId(int userId);

    /**
     * Invalidates all recovery codes associated with a specific user by setting their invalidation timestamp to the current date and time.
     *
     * @param userId the ID of the user whose recovery codes are to be invalidated
     */
    @Modifying
    @Query("UPDATE RecoveryCode rc SET rc.invalidatedAt = NOW() WHERE rc.userId = :userId")
    void invalidateAll(int userId);
}
