package dev.enricosola.porcellino.exception;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.ipGeolocationLookup")
@StandardException
public class IPGeolocationLookupException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4580615021232897640L;
}
