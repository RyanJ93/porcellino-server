package dev.enricosola.porcellino.response;

import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = 2340429701544278004L;
    private static final String DEFAULT_STATUS = "SUCCESS";

    protected final String status;

    public SuccessResponse(String status){
        this.status = status == null || status.isBlank() ? SuccessResponse.DEFAULT_STATUS : status;
    }
}
