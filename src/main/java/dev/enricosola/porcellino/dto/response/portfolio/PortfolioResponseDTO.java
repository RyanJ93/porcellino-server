package dev.enricosola.porcellino.dto.response.portfolio;

import dev.enricosola.porcellino.dto.response.ResponseDTO;
import dev.enricosola.porcellino.entity.Portfolio;
import java.io.Serial;
import lombok.Getter;

@Getter
public class PortfolioResponseDTO extends ResponseDTO {
    @Serial
    private static final long serialVersionUID = 145752698405625142L;

    private final int id;
    private final String name;
    private final String currencyName;

    public PortfolioResponseDTO(Portfolio portfolio) {
        this.currencyName = portfolio.getCurrency().getName();
        this.name = portfolio.getName();
        this.id = portfolio.getId();
    }
}
