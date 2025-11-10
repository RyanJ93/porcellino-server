package dev.enricosola.porcellino.exception.auth.twofactor;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.auth.twoFactor.notInitialized")
@StandardException
public class NotInitializedTwoFactorAuthException extends BaseTwoFactorAuthException {
    @Serial
    private static final long serialVersionUID = -6417598948543532764L;
}
