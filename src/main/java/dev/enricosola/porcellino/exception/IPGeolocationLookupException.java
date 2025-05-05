package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class IPGeolocationLookupException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4580615021232897640L;

    public IPGeolocationLookupException(String message, Throwable cause) {
        super(message, cause);
    }

    public IPGeolocationLookupException(String message) {
        super(message);
    }
}
