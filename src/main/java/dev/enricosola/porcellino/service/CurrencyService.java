package dev.enricosola.porcellino.service;

import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.repository.CurrencyRepository;
import dev.enricosola.porcellino.entity.Currency;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Transactional
@Service
@Slf4j
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    public Currency getById(int id){
        return this.currencyRepository.findById(id).orElse(null);
    }

    public List<Currency> getAll(){
        return this.currencyRepository.findAll();
    }
}
