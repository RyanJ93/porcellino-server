package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class DecryptException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1487160308164731927L;

    public DecryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecryptException(String message) {
        super(message);
    }
}
