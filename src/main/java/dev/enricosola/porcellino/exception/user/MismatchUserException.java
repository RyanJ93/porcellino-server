package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.mismatch")
@StandardException
public class MismatchUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = -4380139986299248449L;
}
