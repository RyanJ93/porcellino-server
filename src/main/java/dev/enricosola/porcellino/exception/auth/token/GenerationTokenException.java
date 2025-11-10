package dev.enricosola.porcellino.exception.auth.token;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.token.generation")
@StandardException
public class GenerationTokenException extends BaseTokenException {
    @Serial
    private static final long serialVersionUID = 6658467802381881554L;
}
