package dev.enricosola.porcellino.controller.v1;

import dev.enricosola.porcellino.dto.response.currency.CurrencyResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import dev.enricosola.porcellino.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/currency")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService){
        this.currencyService = currencyService;
    }

    /**
     * Obtain a list of all the supported currencies.
     */
    @GetMapping
    public ResponseEntity<List<CurrencyResponseDTO>> index() {
        return ResponseEntity.ok().body(this.currencyService.findAll().stream().map(CurrencyResponseDTO::new).toList());
    }
}
