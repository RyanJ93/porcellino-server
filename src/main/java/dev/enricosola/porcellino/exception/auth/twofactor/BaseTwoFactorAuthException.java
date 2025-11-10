package dev.enricosola.porcellino.exception.auth.twofactor;

import dev.enricosola.porcellino.exception.auth.BaseAuthException;
import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.twoFactor")
@StandardException
public abstract class BaseTwoFactorAuthException extends BaseAuthException {
    @Serial
    private static final long serialVersionUID = 8865089599979693342L;
}
