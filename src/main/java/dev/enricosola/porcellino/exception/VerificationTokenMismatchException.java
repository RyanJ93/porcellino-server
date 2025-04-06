package dev.enricosola.porcellino.exception;

public class VerificationTokenMismatchException extends RuntimeException {
    public VerificationTokenMismatchException(String message) {
        super(message);
    }
}
