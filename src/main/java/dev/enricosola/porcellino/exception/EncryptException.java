package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class EncryptException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7597351236467971912L;

    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptException(String message) {
        super(message);
    }
}
