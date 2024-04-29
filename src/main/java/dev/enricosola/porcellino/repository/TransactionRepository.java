package dev.enricosola.porcellino.repository;

import dev.enricosola.porcellino.entity.custom.TransactionTimeSeriesEntry;
import dev.enricosola.porcellino.entity.custom.CumulatedTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import dev.enricosola.porcellino.entity.Transaction;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT new dev.enricosola.porcellino.entity.custom.CumulatedTransactionType(t.type, SUM(t.amount * t.quantity), COUNT(*)) FROM Transaction t WHERE t.portfolio = :portfolio GROUP BY t.type")
    List<CumulatedTransactionType> getCumulatedTransactionType(Portfolio portfolio);

    @Query("SELECT new dev.enricosola.porcellino.entity.custom.CumulatedTransactionType(t.type, SUM(t.amount * t.quantity), COUNT(*)) FROM Transaction t " +
            "WHERE t.portfolio = :portfolio AND t.date <= :endDate GROUP BY t.type")
    List<CumulatedTransactionType> getCumulatedTransactionType(Portfolio portfolio, Date endDate);

    @Query("SELECT new dev.enricosola.porcellino.entity.custom.CumulatedTransactionType(t.type, SUM(t.amount * t.quantity), COUNT(*)) FROM Transaction t " +
            "WHERE t.portfolio = :portfolio AND t.date >= :startDate AND t.date <= :endDate GROUP BY t.type")
    List<CumulatedTransactionType> getCumulatedTransactionType(Portfolio portfolio, Date startDate, Date endDate);

    @Query("SELECT new dev.enricosola.porcellino.entity.custom.TransactionTimeSeriesEntry(t.date, t.type, SUM(t.amount * t.quantity), COUNT(*)) FROM Transaction t" +
            " WHERE t.portfolio = :portfolio AND t.date >= :startDate AND t.date <= :endDate GROUP BY t.date, t.type ORDER BY t.date ASC, t.type ASC")
    List<TransactionTimeSeriesEntry> getTransactionTimeSeries(Portfolio portfolio, Date startDate, Date endDate);

    List<Transaction> findByPortfolio(Portfolio portfolio);
}
