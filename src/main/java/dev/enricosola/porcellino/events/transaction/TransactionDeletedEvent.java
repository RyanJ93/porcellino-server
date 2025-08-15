package dev.enricosola.porcellino.events.transaction;

import dev.enricosola.porcellino.entity.Transaction;
import lombok.Getter;

import java.io.Serial;

public class TransactionDeletedEvent extends TransactionEvent {
    @Serial
    private static final long serialVersionUID = -8206569282827115540L;

    @Getter
    private final Transaction transaction;

    public TransactionDeletedEvent(Object source, Transaction transaction) {
        super(source);

        this.transaction = transaction;
    }
}
