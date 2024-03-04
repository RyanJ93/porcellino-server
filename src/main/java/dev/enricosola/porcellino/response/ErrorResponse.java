package dev.enricosola.porcellino.response;

import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class ErrorResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = -1459748149859760004L;
    private static final String DEFAULT_STATUS = "ERROR";

    protected final String status;

    public ErrorResponse(String status){
        this.status = status == null || status.isBlank() ? ErrorResponse.DEFAULT_STATUS : status;
    }
}
