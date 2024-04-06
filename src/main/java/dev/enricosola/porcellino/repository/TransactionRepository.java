package dev.enricosola.porcellino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.enricosola.porcellino.entity.Transaction;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByPortfolio(Portfolio portfolio);
}
