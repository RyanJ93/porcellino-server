package dev.enricosola.porcellino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.enricosola.porcellino.entity.ClientTracking;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientTrackingRepository extends JpaRepository<ClientTracking, Integer> {
    /**
     * Finds a {@code ClientTracking} entity by its reference name and reference ID.
     *
     * @param refName the reference name used to locate the entity
     * @param refId the reference ID used to locate the entity
     * @return an {@code Optional} containing the found {@code ClientTracking} entity if present, or an empty {@code Optional} if none is found
     */
    Optional<ClientTracking> findByRefNameAndRefId(String refName, int refId);
}
