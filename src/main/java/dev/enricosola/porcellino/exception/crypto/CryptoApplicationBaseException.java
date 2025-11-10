package dev.enricosola.porcellino.exception.crypto;

import dev.enricosola.porcellino.exception.ApplicationBaseException;
import lombok.experimental.StandardException;
import java.io.Serial;

@StandardException
public abstract class CryptoApplicationBaseException extends ApplicationBaseException {
    @Serial
    private static final long serialVersionUID = -6514957352679641728L;
}
