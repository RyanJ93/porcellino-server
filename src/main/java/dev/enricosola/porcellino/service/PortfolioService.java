package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.repository.PortfolioRepository;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.form.portfolio.CreateForm;
import dev.enricosola.porcellino.form.portfolio.EditForm;
import dev.enricosola.porcellino.entity.Portfolio;
import dev.enricosola.porcellino.entity.Currency;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Transactional
@Service
@Slf4j
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final CurrencyService currencyService;

    @Getter
    @Setter
    private Portfolio portfolio;

    public PortfolioService(PortfolioRepository portfolioRepository, CurrencyService currencyService){
        this.portfolioRepository = portfolioRepository;
        this.currencyService = currencyService;
    }

    public Portfolio getById(int id){
        return this.portfolio = this.portfolioRepository.findById(id).orElse(null);
    }

    public List<Portfolio> getAllByUser(User user){
        return this.portfolioRepository.findAllByUser(user);
    }

    public Portfolio createFromForm(User user, CreateForm createForm){
        Currency currency = this.currencyService.getById(createForm.getCurrencyId());
        return this.create(user, currency, createForm.getName());
    }

    public Portfolio updateFromForm(EditForm editForm){
        return this.update(editForm.getName());
    }

    public Portfolio create(User user, Currency currency, String name){
        if ( currency == null ){
            throw new IllegalArgumentException("You must provide a valid currency.");
        }
        if ( user == null ){
            throw new IllegalArgumentException("You must provide a valid user.");
        }
        Portfolio portfolio = new Portfolio();
        portfolio.setCreatedAt(new Date());
        portfolio.setUpdatedAt(new Date());
        portfolio.setCurrency(currency);
        portfolio.setUser(user);
        portfolio.setName(name);
        this.portfolio = this.portfolioRepository.save(portfolio);
        String logMessage = "Created new portfolio \"{}\" by user {} having currency {}.";
        PortfolioService.log.info(logMessage, name, user.getId(), currency.getName());
        return this.portfolio;
    }

    public Portfolio update(String name){
        this.portfolio.setUpdatedAt(new Date());
        this.portfolio.setName(name);
        this.portfolioRepository.save(this.portfolio);
        String logMessage = "Updated portfolio {} with new name \"{}\".";
        PortfolioService.log.info(logMessage, this.portfolio.getId(), name);
        return this.portfolio;
    }

    public void delete(){
        if ( this.portfolio != null ){
            this.portfolioRepository.delete(this.portfolio);
            PortfolioService.log.info("Deleted portfolio {}.", this.portfolio.getId());
            this.portfolio = null;
        }
    }
}
