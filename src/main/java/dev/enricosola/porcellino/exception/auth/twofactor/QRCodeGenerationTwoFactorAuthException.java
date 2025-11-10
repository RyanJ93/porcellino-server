package dev.enricosola.porcellino.exception.auth.twofactor;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.twoFactor.QRCodeGeneration")
@StandardException
public class QRCodeGenerationTwoFactorAuthException extends BaseTwoFactorAuthException {
    @Serial
    private static final long serialVersionUID = 6340887309101388558L;
}
