package dev.enricosola.porcellino.exception;

import java.io.Serial;

public class MalformedVerificationTokenException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 5484601653721724198L;

  public MalformedVerificationTokenException(String message) {
        super(message);
    }
}
