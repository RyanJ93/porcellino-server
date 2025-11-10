package dev.enricosola.porcellino.exception.crypto;

import lombok.experimental.StandardException;
import java.io.Serial;

@StandardException
public class EncryptionFailedCryptoException extends CryptoApplicationBaseException {
    @Serial
    private static final long serialVersionUID = -7597351236467971912L;
}
