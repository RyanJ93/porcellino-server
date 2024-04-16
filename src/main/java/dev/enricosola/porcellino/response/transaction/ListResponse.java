package dev.enricosola.porcellino.response.transaction;

import dev.enricosola.porcellino.dto.TransactionDTO;
import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.response.Response;
import java.io.Serializable;
import java.io.Serial;
import java.util.List;
import lombok.Getter;

@Getter
public class ListResponse extends SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = 9035841581245335129L;

    private final List<TransactionDTO> transactionList;

    public ListResponse(List<TransactionDTO> transactionList){
        super(null);

        this.transactionList = transactionList;
    }
}
