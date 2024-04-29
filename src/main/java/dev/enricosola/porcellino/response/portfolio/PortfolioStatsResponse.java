package dev.enricosola.porcellino.response.portfolio;

import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.dto.PortfolioStatsDTO;
import dev.enricosola.porcellino.response.Response;
import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class PortfolioStatsResponse extends SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = 4091852451451334099L;

    private final PortfolioStatsDTO stats;

    public PortfolioStatsResponse(PortfolioStatsDTO stats){
        super(null);

        this.stats = stats;
    }
}
