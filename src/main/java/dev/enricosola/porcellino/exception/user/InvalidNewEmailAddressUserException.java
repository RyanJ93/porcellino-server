package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.invalidNewEmailAddress")
@StandardException
public class InvalidNewEmailAddressUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = 4742704409372528689L;
}
