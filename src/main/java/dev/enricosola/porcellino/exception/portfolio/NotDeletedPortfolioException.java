package dev.enricosola.porcellino.exception.portfolio;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.portfolio.notDeleted")
@StandardException
public class NotDeletedPortfolioException extends BasePortfolioException {
    @Serial
    private static final long serialVersionUID = 85667438529028895L;
}
