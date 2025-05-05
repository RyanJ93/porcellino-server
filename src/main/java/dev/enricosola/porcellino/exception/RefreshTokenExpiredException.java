package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class RefreshTokenExpiredException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6228030422807433014L;

    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
