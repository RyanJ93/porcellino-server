package dev.enricosola.porcellino.exception;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.unsupportedNotificationType")
@StandardException
public class UnsupportedNotificationTypeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7012409563404600986L;
}
