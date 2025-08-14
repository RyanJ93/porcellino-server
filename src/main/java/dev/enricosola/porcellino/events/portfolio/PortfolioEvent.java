package dev.enricosola.porcellino.events.portfolio;

import org.springframework.context.ApplicationEvent;
import java.io.Serial;

public abstract class PortfolioEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 862917499127316551L;

    public PortfolioEvent(Object source) {
        super(source);
    }
}
