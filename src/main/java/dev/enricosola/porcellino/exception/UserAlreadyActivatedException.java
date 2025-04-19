package dev.enricosola.porcellino.exception;

public class UserAlreadyActivatedException extends RuntimeException {
    public UserAlreadyActivatedException(String message) {
        super(message);
    }
}
