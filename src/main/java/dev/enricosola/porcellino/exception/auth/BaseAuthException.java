package dev.enricosola.porcellino.exception.auth;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import dev.enricosola.porcellino.exception.BaseException;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth")
@StandardException
public abstract class BaseAuthException extends BaseException {
    @Serial
    private static final long serialVersionUID = -4170943797949413986L;
}
