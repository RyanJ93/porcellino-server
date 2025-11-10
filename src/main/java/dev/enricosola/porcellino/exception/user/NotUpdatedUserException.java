package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.notUpdated")
@StandardException
public class NotUpdatedUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = 6827161771954140227L;
}
