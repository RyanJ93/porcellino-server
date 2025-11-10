package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.notActive")
@StandardException
public class NotActiveUserException extends BaseUserException {
    @Serial
    private static final long serialVersionUID = -6777824550192064845L;
}
