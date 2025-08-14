package dev.enricosola.porcellino.events.portfolio;

import dev.enricosola.porcellino.entity.Portfolio;
import java.io.Serial;
import lombok.Getter;

@Getter
public class PortfolioUpdatedEvent extends PortfolioEvent {
    @Serial
    private static final long serialVersionUID = 8520159559658291681L;

    private final Portfolio previousPortfolio;
    private final Portfolio currentPortfolio;

    public PortfolioUpdatedEvent(Object source, Portfolio previousPortfolio, Portfolio currentPortfolio) {
        super(source);

        this.previousPortfolio = previousPortfolio;
        this.currentPortfolio = currentPortfolio;
    }
}
