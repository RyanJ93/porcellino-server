package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class AlreadyEnabledTwoFactorAuthException extends TwoFactorAuthException {
    @Serial
    private static final long serialVersionUID = 4550048852944354102L;

    public AlreadyEnabledTwoFactorAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyEnabledTwoFactorAuthException(String message) {
        super(message);
    }
}
