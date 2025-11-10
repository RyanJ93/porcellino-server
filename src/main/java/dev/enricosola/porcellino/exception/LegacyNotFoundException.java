package dev.enricosola.porcellino.exception;

import java.io.Serial;

@Deprecated
public class LegacyNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3702584011507760998L;

    public LegacyNotFoundException(String message) {
        super(message);
    }
}
