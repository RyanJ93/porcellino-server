package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.malformedVerificationToken")
@StandardException
public class MalformedVerificationTokenUserException extends BaseUserException {
  @Serial
  private static final long serialVersionUID = 5484601653721724198L;
}
