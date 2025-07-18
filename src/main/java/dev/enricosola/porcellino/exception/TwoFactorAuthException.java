package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class TwoFactorAuthException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8865089599979693342L;

    public TwoFactorAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwoFactorAuthException(String message) {
        super(message);
    }
}
