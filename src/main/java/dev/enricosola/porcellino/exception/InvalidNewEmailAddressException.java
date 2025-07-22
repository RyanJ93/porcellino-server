package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class InvalidNewEmailAddressException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4742704409372528689L;

    public InvalidNewEmailAddressException(String message) {
        super(message);
    }

    public InvalidNewEmailAddressException(String message, Throwable cause) {
        super(message, cause);
    }
}
