package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class PasswordMismatchException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7589007998324329176L;

    public PasswordMismatchException(String message) {
        super(message);
    }

    public PasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
