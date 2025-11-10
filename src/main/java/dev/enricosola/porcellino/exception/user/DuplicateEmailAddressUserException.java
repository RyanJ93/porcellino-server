package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.duplicateEmailAddress")
@StandardException
public class DuplicateEmailAddressUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = -7377976937794209347L;
}
