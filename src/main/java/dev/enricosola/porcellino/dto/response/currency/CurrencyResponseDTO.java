package dev.enricosola.porcellino.dto.response.currency;

import dev.enricosola.porcellino.dto.response.ResponseDTO;
import dev.enricosola.porcellino.entity.Currency;
import java.io.Serial;
import lombok.Getter;

@Getter
public class CurrencyResponseDTO extends ResponseDTO {
    @Serial
    private static final long serialVersionUID = -224954582374086270L;

    private final int id;
    private final String code;
    private final String name;
    private final String symbol;

    public CurrencyResponseDTO(Currency currency) {
        this.symbol = currency.getSymbol();
        this.code = currency.getCode();
        this.name = currency.getName();
        this.id = currency.getId();
    }
}
