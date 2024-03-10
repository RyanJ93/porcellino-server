package dev.enricosola.porcellino.response.currency;

import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.response.Response;
import dev.enricosola.porcellino.entity.Currency;
import java.io.Serializable;
import java.util.List;
import java.io.Serial;
import lombok.Getter;

@Getter
public class ListResponse extends SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = -9161700181432910919L;

    private final List<Currency> currencyList;

    public ListResponse(List<Currency> currencyList){
        super(null);

        this.currencyList = currencyList;
    }
}
