package dev.enricosola.porcellino.exception.portfolio;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import dev.enricosola.porcellino.exception.BaseException;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.portfolio")
@StandardException
public abstract class BasePortfolioException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1967806457831702405L;
}
