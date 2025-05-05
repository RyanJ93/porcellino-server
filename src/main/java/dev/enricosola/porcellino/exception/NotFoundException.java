package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class NotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3702584011507760998L;

    public NotFoundException(String message) {
        super(message);
    }
}
