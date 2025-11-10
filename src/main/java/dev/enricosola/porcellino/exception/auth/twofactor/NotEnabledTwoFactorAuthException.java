package dev.enricosola.porcellino.exception.auth.twofactor;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.twoFactor.notEnabled")
@StandardException
public class NotEnabledTwoFactorAuthException extends BaseTwoFactorAuthException {
    @Serial
    private static final long serialVersionUID = 7208063237195571902L;
}
