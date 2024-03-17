package dev.enricosola.porcellino.response.portfolio;

import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.response.Response;
import dev.enricosola.porcellino.dto.PortfolioDTO;
import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class EditResponse extends SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = -4279189875839848286L;

    private final PortfolioDTO portfolio;

    public EditResponse(PortfolioDTO portfolio){
        super(null);

        this.portfolio = portfolio;
    }
}
