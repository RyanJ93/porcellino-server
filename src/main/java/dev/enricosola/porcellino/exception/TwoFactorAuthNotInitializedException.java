package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class TwoFactorAuthNotInitializedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6417598948543532764L;

    public TwoFactorAuthNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwoFactorAuthNotInitializedException(String message) {
        super(message);
    }
}
