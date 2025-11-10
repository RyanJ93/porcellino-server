package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.portfolio.NotUpdatedPortfolioException;
import dev.enricosola.porcellino.exception.portfolio.NotDeletedPortfolioException;
import dev.enricosola.porcellino.exception.portfolio.NotFoundPortfolioException;
import dev.enricosola.porcellino.dto.service.portfolio.PortfolioCreateDTO;
import dev.enricosola.porcellino.dto.service.portfolio.PortfolioUpdateDTO;
import dev.enricosola.porcellino.events.portfolio.PortfolioCreatedEvent;
import dev.enricosola.porcellino.events.portfolio.PortfolioDeletedEvent;
import dev.enricosola.porcellino.events.portfolio.PortfolioUpdatedEvent;
import dev.enricosola.porcellino.exception.user.NotCreatedUserException;
import dev.enricosola.porcellino.exception.user.NotFoundUserException;
import dev.enricosola.porcellino.repository.PortfolioRepository;
import org.springframework.context.ApplicationEventPublisher;
import dev.enricosola.porcellino.entity.Portfolio;
import dev.enricosola.porcellino.entity.Currency;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PortfolioRepository portfolioRepository;
    private final CurrencyService currencyService;
    private final UserService userService;

    /**
     * Retrieves all portfolios from the repository.
     *
     * @return a list of Portfolio objects representing all the portfolios available in the repository.
     */
    public List<Portfolio> findAll() {
        return this.portfolioRepository.findAll();
    }

    /**
     * Retrieves all portfolios associated with a specific user identifier.
     *
     * @param userId the unique identifier of the user whose portfolios are to be retrieved.
     * @return a list of Portfolio objects associated with the given user identifier.
     */
    public List<Portfolio> findAllByUserId(int userId) {
        return this.portfolioRepository.findAllByUserId(userId);
    }

    /**
     * Retrieves a portfolio by its unique identifier.
     *
     * @param id the unique identifier of the portfolio to be retrieved.
     * @return the Portfolio object corresponding to the given identifier.
     * @throws NotFoundPortfolioException if no portfolio is found with the given identifier.
     */
    public Portfolio find(int id) {
        return this.portfolioRepository.findById(id)
                .orElseThrow(() -> new NotFoundPortfolioException("No matching portfolio found."));
    }

    /**
     * Creates a new Portfolio entity and persists it in the repository.
     *
     * @param userId the unique identifier of the user who owns the portfolio.
     * @param portfolioCreateDTO the data transfer object containing the details of the portfolio to be created.
     * @return the newly created Portfolio entity.
     * @throws NotFoundUserException If no user matching the given email address is found.
     * @throws NotCreatedUserException If the portfolio could not be created.
     */
    public Portfolio create(int userId, PortfolioCreateDTO portfolioCreateDTO) {
        Currency currency = this.currencyService.findById(portfolioCreateDTO.getCurrencyId());
        User user = this.userService.find(userId);
        try {
            Portfolio portfolio = portfolioCreateDTO.toEntity();
            portfolio.setCurrency(currency);
            portfolio.setUser(user);
            portfolio = this.portfolioRepository.save(portfolio);
            this.applicationEventPublisher.publishEvent(new PortfolioCreatedEvent(this, portfolio));
            return portfolio;
        } catch (Exception ex) {
            throw new NotCreatedUserException("Could not create portfolio.", ex);
        }
    }

    /**
     * Updates an existing Portfolio entity with the provided details and persists the changes.
     *
     * @param id the unique identifier of the portfolio to be updated.
     * @param portfolioUpdateDTO the data transfer object containing the updated details of the portfolio.
     * @return the updated Portfolio entity.
     * @throws NotFoundPortfolioException if no portfolio is found with the given identifier.
     * @throws NotUpdatedPortfolioException if the portfolio could not be updated.
     */
    public Portfolio update(int id, PortfolioUpdateDTO portfolioUpdateDTO) {
        Portfolio portfolio = this.find(id);
        try {
            Portfolio previousPortfolio = new ModelMapper().map(portfolio, Portfolio.class);
            portfolio = portfolioUpdateDTO.hydrateEntity(portfolio);
            portfolio = this.portfolioRepository.save(portfolio);
            this.applicationEventPublisher.publishEvent(new PortfolioUpdatedEvent(this, previousPortfolio, portfolio));
            return portfolio;
        } catch (Exception ex) {
            throw new NotUpdatedPortfolioException("Could not update portfolio.", ex);
        }
    }

    /**
     * Deletes a portfolio by its unique identifier.
     *
     * @param id the unique identifier of the portfolio to be deleted.
     * @throws NotFoundPortfolioException if no portfolio is found with the given identifier.
     * @throws NotDeletedPortfolioException if the portfolio could not be deleted.
     */
    public void delete(int id) {
        Portfolio portfolio = this.find(id);
        try {
            this.portfolioRepository.delete(portfolio);
            this.applicationEventPublisher.publishEvent(new PortfolioDeletedEvent(this, portfolio));
        } catch (Exception ex) {
            throw new NotDeletedPortfolioException("Could not delete portfolio.", ex);
        }
    }
}
