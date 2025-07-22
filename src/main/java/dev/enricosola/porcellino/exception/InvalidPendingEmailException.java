package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class InvalidPendingEmailException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -46504576643588778L;

    public InvalidPendingEmailException(String message) {
        super(message);
    }

    public InvalidPendingEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
