package dev.enricosola.porcellino.exception;

import java.io.Serial;

@Deprecated
public class TokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7081576365029595232L;

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(String message) {
        super(message);
    }
}
