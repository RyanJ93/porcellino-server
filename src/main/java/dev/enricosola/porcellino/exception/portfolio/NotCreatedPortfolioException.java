package dev.enricosola.porcellino.exception.portfolio;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.portfolio.notCreated")
@StandardException
public class NotCreatedPortfolioException extends BasePortfolioException {
    @Serial
    private static final long serialVersionUID = 3041811616002327813L;
}
