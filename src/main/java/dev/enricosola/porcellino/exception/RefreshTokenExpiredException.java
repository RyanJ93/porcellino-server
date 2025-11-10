package dev.enricosola.porcellino.exception;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.refreshTokenExpired")
@StandardException
public class RefreshTokenExpiredException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6228030422807433014L;
}
