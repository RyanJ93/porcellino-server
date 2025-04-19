package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class DuplicateEmailAddressException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7377976937794209347L;

    public DuplicateEmailAddressException(String message) {
        super(message);
    }

    public DuplicateEmailAddressException(String message, Throwable cause) {
        super(message, cause);
    }
}
