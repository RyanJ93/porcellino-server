package dev.enricosola.porcellino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.enricosola.porcellino.entity.Currency;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

}
