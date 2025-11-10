package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.dto.service.transaction.TransactionCreateDTO;
import dev.enricosola.porcellino.dto.service.transaction.TransactionUpdateDTO;
import dev.enricosola.porcellino.events.transaction.TransactionCreatedEvent;
import dev.enricosola.porcellino.events.transaction.TransactionDeletedEvent;
import dev.enricosola.porcellino.events.transaction.TransactionUpdatedEvent;
import dev.enricosola.porcellino.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.exception.LegacyNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import dev.enricosola.porcellino.entity.Transaction;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Transactional
@Service
@Slf4j
public class TransactionService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TransactionRepository transactionRepository;
    private final PortfolioService portfolioService;

    public TransactionService(
            ApplicationEventPublisher applicationEventPublisher,
            TransactionRepository transactionRepository,
            PortfolioService portfolioService
    ){
        this.applicationEventPublisher = applicationEventPublisher;
        this.transactionRepository = transactionRepository;
        this.portfolioService = portfolioService;
    }

    /**
     * Retrieves all transactions associated with a specific portfolio.
     *
     * @param portfolioId the unique identifier of the portfolio whose transactions are to be retrieved.
     * @return a list of transactions associated with the specified portfolio.
     */
    public List<Transaction> findAll(int portfolioId) {
        Portfolio portfolio = this.portfolioService.find(portfolioId);
        return this.transactionRepository.findAllByPortfolio(portfolio);
    }

    /**
     * Finds a transaction by its unique identifier and associated portfolio identifier.
     *
     * @param portfolioId the unique identifier of the portfolio to which the transaction belongs.
     * @param id the unique identifier of the transaction to be retrieved.
     * @return the transaction matching the given identifiers.
     * @throws LegacyNotFoundException if no transaction is found matching the provided identifiers.
     */
    public Transaction find(int portfolioId, int id) {
        return this.transactionRepository.findByIdAndPortfolioId(id, portfolioId)
                .orElseThrow(() -> new LegacyNotFoundException("No matching transaction found."));
    }

    /**
     * Creates a new transaction for the specified portfolio.
     *
     * @param portfolioId the unique identifier of the portfolio to which the transaction belongs.
     * @param transactionCreateDTO the data transfer object containing the details of the transaction to be created.
     * @return the newly created Transaction object.
     */
    public Transaction create(int portfolioId, TransactionCreateDTO transactionCreateDTO) {
        Portfolio portfolio = this.portfolioService.find(portfolioId);
        Transaction transaction = transactionCreateDTO.toEntity();
        transaction.setPortfolio(portfolio);
        transaction = this.transactionRepository.save(transaction);
        this.applicationEventPublisher.publishEvent(new TransactionCreatedEvent(this, transaction));
        return transaction;
    }

    /**
     * Updates an existing transaction with the specified details.
     *
     * @param portfolioId the unique identifier of the portfolio to which the transaction belongs.
     * @param id the unique identifier of the transaction to be updated.
     * @param transactionUpdateDTO the data transfer object containing the updated details for the transaction.
     * @return the updated Transaction object after persisting changes.
     * @throws LegacyNotFoundException if no transaction is found matching the provided identifiers.
     */
    public Transaction update(int portfolioId, int id, TransactionUpdateDTO transactionUpdateDTO) {
        Transaction transaction = this.find(portfolioId, id);
        Transaction previousTransaction = new ModelMapper().map(transaction, Transaction.class);
        transaction = transactionUpdateDTO.hydrateEntity(transaction);
        transaction = this.transactionRepository.save(transaction);
        this.applicationEventPublisher.publishEvent(new TransactionUpdatedEvent(this, previousTransaction, transaction));
        return transaction;
    }

    /**
     * Deletes a transaction associated with the specified portfolio.
     *
     * @param portfolioId the unique identifier of the portfolio to which the transaction belongs.
     * @param id the unique identifier of the transaction to be deleted.
     * @throws LegacyNotFoundException if no transaction is found matching the provided identifiers.
     */
    public void delete(int portfolioId, int id) {
        Transaction transaction = this.find(portfolioId, id);
        this.transactionRepository.delete(transaction);
        this.applicationEventPublisher.publishEvent(new TransactionDeletedEvent(this, transaction));
    }
}
