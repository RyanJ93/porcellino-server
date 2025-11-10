package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.alreadyActivated")
@StandardException
public class AlreadyActivatedUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = 4307002957595353117L;
}
