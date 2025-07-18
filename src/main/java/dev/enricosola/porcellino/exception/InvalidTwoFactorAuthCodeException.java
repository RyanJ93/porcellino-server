package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class InvalidTwoFactorAuthCodeException extends TwoFactorAuthException {
    @Serial
    private static final long serialVersionUID = -8638976185315660203L;

    public InvalidTwoFactorAuthCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTwoFactorAuthCodeException(String message) {
        super(message);
    }
}
