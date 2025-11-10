package dev.enricosola.porcellino.exception;

public interface HttpStatusCodeAware {
    /**
     * Retrieves the HTTP status code associated with the current context.
     *
     * @return the HTTP status code as an integer.
     */
    int getHttpStatusCode();
}
