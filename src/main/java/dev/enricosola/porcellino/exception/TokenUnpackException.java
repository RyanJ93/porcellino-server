package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class TokenUnpackException extends TokenException {
    @Serial
    private static final long serialVersionUID = 3640350942519256576L;

    public TokenUnpackException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenUnpackException(String message) {
        super(message);
    }
}
