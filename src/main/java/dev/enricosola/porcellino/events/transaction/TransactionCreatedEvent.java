package dev.enricosola.porcellino.events.transaction;

import dev.enricosola.porcellino.entity.Transaction;
import java.io.Serial;
import lombok.Getter;

public class TransactionCreatedEvent extends TransactionEvent {
    @Serial
    private static final long serialVersionUID = -8206569282827115540L;

    @Getter
    private final Transaction transaction;

    public TransactionCreatedEvent(Object source, Transaction transaction) {
        super(source);

        this.transaction = transaction;
    }
}
