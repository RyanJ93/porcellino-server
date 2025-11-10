package dev.enricosola.porcellino.exception.auth;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.failed")
@StandardException
public class FailedAuthException extends BaseAuthException {
    @Serial
    private static final long serialVersionUID = -3883040657530275905L;
}
