package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.passwordMismatch")
@StandardException
public class PasswordMismatchUserException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7589007998324329176L;
}
