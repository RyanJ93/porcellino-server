package dev.enricosola.porcellino.exception.auth.twofactor;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.twoFactor.alreadyEnabled")
@StandardException
public class AlreadyEnabledTwoFactorAuthException extends BaseTwoFactorAuthException {
    @Serial
    private static final long serialVersionUID = 4550048852944354102L;
}
