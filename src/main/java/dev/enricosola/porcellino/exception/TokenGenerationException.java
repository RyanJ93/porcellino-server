package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class TokenGenerationException extends TokenException {
    @Serial
    private static final long serialVersionUID = 6658467802381881554L;

    public TokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenGenerationException(String message) {
        super(message);
    }
}
