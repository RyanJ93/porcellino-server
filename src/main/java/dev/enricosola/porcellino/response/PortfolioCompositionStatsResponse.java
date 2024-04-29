package dev.enricosola.porcellino.response;

import dev.enricosola.porcellino.dto.PortfolioCompositionStatsDTO;
import dev.enricosola.porcellino.dto.PortfolioStatsDTO;
import java.io.Serializable;
import java.io.Serial;
import lombok.Getter;

@Getter
public class PortfolioCompositionStatsResponse extends SuccessResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = 1724854657709923533L;

    private final PortfolioCompositionStatsDTO compositionStats;
    private final PortfolioStatsDTO stats;

    public PortfolioCompositionStatsResponse(PortfolioCompositionStatsDTO compositionStats, PortfolioStatsDTO stats){
        super(null);

        this.compositionStats = compositionStats;
        this.stats = stats;
    }
}
