package dev.enricosola.porcellino.response;

import java.io.Serializable;
import java.io.Serial;

public class ExceptionErrorResponse extends ErrorResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = 1580466671299375594L;

    protected final String error;

    public ExceptionErrorResponse(Throwable ex, String status){
        super(status);

        this.error = ex.getLocalizedMessage();
    }
}
