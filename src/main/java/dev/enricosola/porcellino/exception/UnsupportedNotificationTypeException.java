package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class UnsupportedNotificationTypeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7012409563404600986L;

    public UnsupportedNotificationTypeException(String message) {
        super(message);
    }
}
