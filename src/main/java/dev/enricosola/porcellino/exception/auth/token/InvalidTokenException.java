package dev.enricosola.porcellino.exception.auth.token;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.token.invalid")
@StandardException
public class InvalidTokenException extends BaseTokenException {
    @Serial
    private static final long serialVersionUID = 3411470345668019683L;
}
