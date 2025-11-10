package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.BaseException;
import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user")
@StandardException
public abstract class BaseUserException extends BaseException {
    @Serial
    private static final long serialVersionUID = 8387339544735177426L;
}
