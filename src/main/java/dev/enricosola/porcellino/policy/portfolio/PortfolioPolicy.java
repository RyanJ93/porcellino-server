package dev.enricosola.porcellino.policy.portfolio;

import dev.enricosola.porcellino.service.PortfolioService;
import dev.enricosola.porcellino.policy.BasePolicy;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.stereotype.Component;

@Component("portfolioPolicy")
public class PortfolioPolicy extends BasePolicy {
    private final PortfolioService portfolioService;

    public PortfolioPolicy(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * Determines whether the currently authenticated user has access to the portfolio with the given identifier.
     *
     * @param id the unique identifier of the portfolio to be checked for access.
     * @return true if the currently authenticated user is the owner of the specified portfolio, otherwise false.
     */
    public boolean userCanAccess(int id) {
        Portfolio portfolio = this.portfolioService.find(id);
        return portfolio.getUser().getId() == this.getAuthenticatedUser().getId();
    }
}
