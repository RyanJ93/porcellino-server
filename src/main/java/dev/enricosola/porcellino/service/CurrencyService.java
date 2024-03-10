package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.repository.CurrencyRepository;
import dev.enricosola.porcellino.entity.Currency;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> getAll(){
        return this.currencyRepository.findAll();
    }
}
