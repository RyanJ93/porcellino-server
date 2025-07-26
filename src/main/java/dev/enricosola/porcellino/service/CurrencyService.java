package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.repository.CurrencyRepository;
import dev.enricosola.porcellino.exception.NotFoundException;
import dev.enricosola.porcellino.entity.Currency;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Retrieves a Currency entity by its unique identifier.
     *
     * @param id the unique identifier of the Currency entity to retrieve.
     * @return the Currency entity associated with the specified identifier.
     * @throws NotFoundException if no Currency entity with the given identifier is found.
     */
    public Currency findById(int id) {
        return this.currencyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No matching currency found."));
    }

    /**
     * Retrieves all available Currency entities from the repository.
     *
     * @return a list of all Currency entities.
     */
    public List<Currency> findAll() {
        return this.currencyRepository.findAll();
    }
}
