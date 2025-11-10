package dev.enricosola.porcellino.exception.auth.token;

import dev.enricosola.porcellino.exception.auth.BaseAuthException;
import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.token")
@StandardException
public abstract class BaseTokenException extends BaseAuthException {
    @Serial
    private static final long serialVersionUID = -3640113145724133233L;
}
