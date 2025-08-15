package dev.enricosola.porcellino.policy.transaction;

import dev.enricosola.porcellino.service.TransactionService;
import dev.enricosola.porcellino.entity.Transaction;
import dev.enricosola.porcellino.policy.BasePolicy;
import org.springframework.stereotype.Component;

@Component("transactionPolicy")
public class TransactionPolicy extends BasePolicy {
    private final TransactionService transactionService;

    public TransactionPolicy(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Determines whether the authenticated user is allowed to access a specific transaction within a portfolio.
     *
     * @param portfolioId the unique identifier of the portfolio associated with the transaction.
     * @param id the unique identifier of the transaction to check access for.
     * @return true if the authenticated user is the owner of the portfolio associated with the transaction, false otherwise.
     */
    public boolean userCanAccess(int portfolioId, int id) {
        Transaction transaction = this.transactionService.find(portfolioId, id);
        return transaction.getPortfolio().getUser().getId() == this.getAuthenticatedUser().getId();
    }
}
