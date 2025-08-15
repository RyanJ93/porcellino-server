package dev.enricosola.porcellino.events.transaction;

import org.springframework.context.ApplicationEvent;
import java.io.Serial;

public abstract class TransactionEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = -4172013057207383335L;

    public TransactionEvent(Object source) {
        super(source);
    }
}
