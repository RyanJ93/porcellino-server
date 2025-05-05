package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class InvalidTokenException extends TokenException {

    @Serial
    private static final long serialVersionUID = 3411470345668019683L;

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
