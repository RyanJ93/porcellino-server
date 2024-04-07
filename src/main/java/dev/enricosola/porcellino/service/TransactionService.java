package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.form.transaction.CreateForm;
import dev.enricosola.porcellino.form.transaction.EditForm;
import dev.enricosola.porcellino.enums.TransactionType;
import dev.enricosola.porcellino.entity.Transaction;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Transactional
@Service
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Getter
    @Setter
    private Transaction transaction;

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public Transaction getById(int id){
        return this.transaction = this.transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> getAll(Portfolio portfolio){
        return this.transactionRepository.findByPortfolio(portfolio);
    }

    public Transaction createFromForm(Portfolio portfolio, CreateForm createForm){
        return this.create(
            portfolio,
            createForm.getAmount(),
            createForm.getQuantity(),
            createForm.getType(),
            createForm.getDate(),
            createForm.getNote()
        );
    }

    public Transaction editFromForm(EditForm editForm){
        return this.edit(
            editForm.getAmount(),
            editForm.getQuantity(),
            editForm.getType(),
            editForm.getDate(),
            editForm.getNote()
        );
    }

    public Transaction create(Portfolio portfolio, double amount, int quantity, TransactionType type, Date date, String note){
        Transaction transaction = new Transaction();
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        transaction.setPortfolio(portfolio);
        transaction.setQuantity(quantity);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDate(date);
        transaction.setNote(note);
        String logMessage = "Created new transaction with type {}, amount {} and quantity {} at date {} and contained in portfolio {}.";
        TransactionService.log.info(logMessage, type, amount, quantity, date, portfolio.getId());
        return this.transaction = this.transactionRepository.save(transaction);
    }

    public Transaction edit(double amount, int quantity, TransactionType type, Date date, String note){
        this.transaction.setUpdatedAt(new Date());
        this.transaction.setQuantity(quantity);
        this.transaction.setAmount(amount);
        this.transaction.setType(type);
        this.transaction.setDate(date);
        this.transaction.setNote(note);
        String logMessage = "Updated transaction {} with type {}, amount {} and quantity {} at date {}.";
        TransactionService.log.info(logMessage, this.transaction.getId(), type, amount, quantity, date);
        return this.transactionRepository.save(this.transaction);
    }

    public void delete(){
        this.transactionRepository.delete(this.transaction);
        TransactionService.log.info("Deleted transaction {}.", this.transaction.getId());
        this.transaction = null;
    }
}
