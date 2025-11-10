package dev.enricosola.porcellino.exception.clienttracking;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.clientTracking.notFound")
@StandardException
public class NotFoundClientTrackingException extends BaseClientTrackingException {
    @Serial
    private static final long serialVersionUID = 6477359590214169105L;
}
