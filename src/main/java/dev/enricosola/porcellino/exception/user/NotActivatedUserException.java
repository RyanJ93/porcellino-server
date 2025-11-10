package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.notActivated")
@StandardException
public class NotActivatedUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = -8032293799799805406L;
}
