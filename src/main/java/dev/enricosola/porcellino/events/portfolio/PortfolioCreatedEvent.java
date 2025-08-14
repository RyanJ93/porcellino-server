package dev.enricosola.porcellino.events.portfolio;

import dev.enricosola.porcellino.entity.Portfolio;
import java.io.Serial;
import lombok.Getter;

@Getter
public class PortfolioCreatedEvent extends PortfolioEvent {
    @Serial
    private static final long serialVersionUID = 6307155227280873243L;

    private final Portfolio portfolio;

    public PortfolioCreatedEvent(Object source, Portfolio portfolio) {
        super(source);

        this.portfolio = portfolio;
    }
}
