package dev.enricosola.porcellino.exception.auth.twofactor;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.twoFactor.invalidCode")
@StandardException
public class InvalidCodeTwoFactorAuthException extends BaseTwoFactorAuthException {
    @Serial
    private static final long serialVersionUID = -8638976185315660203L;
}
