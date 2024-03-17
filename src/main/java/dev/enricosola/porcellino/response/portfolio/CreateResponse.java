package dev.enricosola.porcellino.response.portfolio;

import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.response.Response;
import dev.enricosola.porcellino.dto.PortfolioDTO;
import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class CreateResponse extends SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = 1952805122405130410L;

    private final PortfolioDTO portfolio;

    public CreateResponse(PortfolioDTO portfolio){
        super(null);

        this.portfolio = portfolio;
    }
}
