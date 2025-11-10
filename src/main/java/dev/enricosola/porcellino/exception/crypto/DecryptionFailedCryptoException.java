package dev.enricosola.porcellino.exception.crypto;

import lombok.experimental.StandardException;
import java.io.Serial;

@StandardException
public class DecryptionFailedCryptoException extends CryptoApplicationBaseException {
    @Serial
    private static final long serialVersionUID = 1487160308164731927L;
}
