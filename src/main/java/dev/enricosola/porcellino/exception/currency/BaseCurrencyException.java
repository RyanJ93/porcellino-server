package dev.enricosola.porcellino.exception.currency;

import dev.enricosola.porcellino.exception.BaseException;
import lombok.experimental.StandardException;
import java.io.Serial;

@StandardException
public abstract class BaseCurrencyException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1498143926419356981L;
}
