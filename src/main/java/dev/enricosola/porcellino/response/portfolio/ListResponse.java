package dev.enricosola.porcellino.response.portfolio;

import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.response.Response;
import dev.enricosola.porcellino.dto.PortfolioDTO;
import java.io.Serializable;
import java.io.Serial;
import java.util.List;
import lombok.Getter;

@Getter
public class ListResponse extends SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = -6282654993147316203L;

    private final List<PortfolioDTO> portfolioList;

    public ListResponse(List<PortfolioDTO> portfolioList){
        super(null);

        this.portfolioList = portfolioList;
    }
}
