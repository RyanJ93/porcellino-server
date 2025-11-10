package dev.enricosola.porcellino.exception.currency;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@StandardException
@Identifier("exception.currency.notFound")
public class NotFoundCurrencyException extends BaseCurrencyException {
    @Serial
    private static final long serialVersionUID = 4205695196457901606L;
}
