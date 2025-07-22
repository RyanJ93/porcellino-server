package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class UserMismatchException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4380139986299248449L;

    public UserMismatchException(String message) {
        super(message);
    }

    public UserMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
