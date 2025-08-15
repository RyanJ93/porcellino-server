package dev.enricosola.porcellino.repository;

import dev.enricosola.porcellino.entity.custom.TransactionTimeSeriesEntry;
import dev.enricosola.porcellino.entity.custom.CumulatedTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import dev.enricosola.porcellino.entity.Transaction;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    /**
     * Retrieves an optional {@link Transaction} based on its unique identifier and associated portfolio identifier.
     *
     * @param id the unique identifier of the transaction
     * @param portfolioId the identifier of the portfolio associated with the transaction
     * @return an {@link Optional} containing the transaction if found, or empty if no such transaction exists
     */
    Optional<Transaction> findByIdAndPortfolioId(int id, int portfolioId);

    /**
     * Retrieves all transactions associated with a specific portfolio.
     *
     * @param portfolio the portfolio whose transactions are to be retrieved
     * @return a list of transactions associated with the given portfolio
     */
    List<Transaction> findAllByPortfolio(Portfolio portfolio);

    /**
     * @deprecated
     */
    @Query("SELECT new dev.enricosola.porcellino.entity.custom.CumulatedTransactionType(t.type, SUM(t.amount * t.quantity), COUNT(*)) FROM Transaction t WHERE t.portfolio = :portfolio GROUP BY t.type")
    List<CumulatedTransactionType> getCumulatedTransactionType(Portfolio portfolio);

    /**
     * @deprecated
     */
    @Query("SELECT new dev.enricosola.porcellino.entity.custom.CumulatedTransactionType(t.type, SUM(t.amount * t.quantity), COUNT(*)) FROM Transaction t " +
            "WHERE t.portfolio = :portfolio AND t.date <= :endDate GROUP BY t.type")
    List<CumulatedTransactionType> getCumulatedTransactionType(Portfolio portfolio, Date endDate);

    /**
     * @deprecated
     */
    @Query("SELECT new dev.enricosola.porcellino.entity.custom.CumulatedTransactionType(t.type, SUM(t.amount * t.quantity), COUNT(*)) FROM Transaction t " +
            "WHERE t.portfolio = :portfolio AND t.date >= :startDate AND t.date <= :endDate GROUP BY t.type")
    List<CumulatedTransactionType> getCumulatedTransactionType(Portfolio portfolio, Date startDate, Date endDate);

    /**
     * @deprecated
     */
    @Query("SELECT new dev.enricosola.porcellino.entity.custom.TransactionTimeSeriesEntry(t.date, t.type, SUM(t.amount * t.quantity), COUNT(*)) FROM Transaction t" +
            " WHERE t.portfolio = :portfolio AND t.date >= :startDate AND t.date <= :endDate GROUP BY t.date, t.type ORDER BY t.date ASC, t.type ASC")
    List<TransactionTimeSeriesEntry> getTransactionTimeSeries(Portfolio portfolio, Date startDate, Date endDate);
}
