package dev.enricosola.porcellino.exception.auth.token;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.token.unpack")
@StandardException
public class UnpackTokenException extends BaseTokenException {
    @Serial
    private static final long serialVersionUID = 3640350942519256576L;
}
