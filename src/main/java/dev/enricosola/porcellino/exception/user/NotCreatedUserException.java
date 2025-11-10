package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.notCreated")
@StandardException
public class NotCreatedUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = 1129032185999949806L;
}
