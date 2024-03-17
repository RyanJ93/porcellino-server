package dev.enricosola.porcellino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.stereotype.Repository;
import dev.enricosola.porcellino.entity.User;
import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {
    List<Portfolio> findAllByUser(User user);
}
