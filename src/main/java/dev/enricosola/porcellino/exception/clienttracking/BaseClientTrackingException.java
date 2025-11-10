package dev.enricosola.porcellino.exception.clienttracking;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import dev.enricosola.porcellino.exception.BaseException;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.clientTracking")
@StandardException
public abstract class BaseClientTrackingException extends BaseException {
    @Serial
    private static final long serialVersionUID = -6227677792583462436L;
}
