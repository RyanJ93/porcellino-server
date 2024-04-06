package dev.enricosola.porcellino.response.transaction;

import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.dto.TransactionDTO;
import dev.enricosola.porcellino.response.Response;
import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class EditResponse extends SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = -1893270092826121094L;

    private final TransactionDTO transaction;

    public EditResponse(TransactionDTO transaction){
        super(null);

        this.transaction = transaction;
    }
}
