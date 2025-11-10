package dev.enricosola.porcellino.exception.portfolio;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@StandardException
@Identifier("exception.portfolio.notfound")
public class NotFoundPortfolioException extends BasePortfolioException {
    @Serial
    private static final long serialVersionUID = 2574862129167399482L;
}
