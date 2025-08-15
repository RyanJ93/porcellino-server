package dev.enricosola.porcellino.events.transaction;

import dev.enricosola.porcellino.entity.Transaction;
import java.io.Serial;
import lombok.Getter;

public class TransactionUpdatedEvent extends TransactionEvent {
    @Serial
    private static final long serialVersionUID = 169080553013381749L;

    @Getter
    private final Transaction previousTransaction;

    @Getter
    private final Transaction currentTransaction;

    public TransactionUpdatedEvent(Object source, Transaction previousTransaction, Transaction currentTransaction) {
        super(source);

        this.previousTransaction = previousTransaction;
        this.currentTransaction = currentTransaction;
    }
}
