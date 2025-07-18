package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class NotEnabledTwoFactorAuthException extends TwoFactorAuthException {
    @Serial
    private static final long serialVersionUID = 7208063237195571902L;

    public NotEnabledTwoFactorAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnabledTwoFactorAuthException(String message) {
        super(message);
    }
}
