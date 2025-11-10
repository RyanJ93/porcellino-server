package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.verificationTokenMismatch")
@StandardException
public class VerificationTokenMismatchUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = 4816272084544238920L;
}
