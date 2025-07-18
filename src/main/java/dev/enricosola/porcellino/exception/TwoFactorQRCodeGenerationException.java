package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class TwoFactorQRCodeGenerationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6340887309101388558L;

    public TwoFactorQRCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwoFactorQRCodeGenerationException(String message) {
        super(message);
    }
}
