package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.currency.NotFoundCurrencyException;
import dev.enricosola.porcellino.repository.CurrencyRepository;
import dev.enricosola.porcellino.entity.Currency;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    /**
     * Retrieves a Currency entity by its unique identifier.
     *
     * @param id The unique identifier of the Currency entity to retrieve.
     * @return The Currency entity associated with the specified identifier.
     * @throws NotFoundCurrencyException If no Currency entity with the given identifier is found.
     */
    public Currency findById(int id) {
        return this.currencyRepository.findById(id)
                .orElseThrow(() -> new NotFoundCurrencyException("No matching currency found."));
    }

    /**
     * Retrieves all available Currency entities from the repository.
     *
     * @return A list of all Currency entities.
     */
    public List<Currency> findAll() {
        return this.currencyRepository.findAll();
    }
}
