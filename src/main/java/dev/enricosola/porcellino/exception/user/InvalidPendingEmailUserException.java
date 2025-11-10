package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.invalidPendingEmail")
@StandardException
public class InvalidPendingEmailUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = -46504576643588778L;
}
