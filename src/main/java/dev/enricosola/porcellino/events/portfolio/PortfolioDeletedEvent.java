package dev.enricosola.porcellino.events.portfolio;

import dev.enricosola.porcellino.entity.Portfolio;
import java.io.Serial;
import lombok.Getter;

@Getter
public class PortfolioDeletedEvent extends PortfolioEvent {
    @Serial
    private static final long serialVersionUID = -5470547864322259050L;

    private final Portfolio portfolio;

    public PortfolioDeletedEvent(Object source, Portfolio portfolio) {
        super(source);

        this.portfolio = portfolio;
    }
}
