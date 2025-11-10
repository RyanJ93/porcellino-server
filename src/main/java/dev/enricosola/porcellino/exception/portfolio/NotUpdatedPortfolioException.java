package dev.enricosola.porcellino.exception.portfolio;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.portfolio.notUpdated")
@StandardException
public class NotUpdatedPortfolioException extends BasePortfolioException {
    @Serial
    private static final long serialVersionUID = 2544336106845456312L;
}
