package dev.enricosola.porcellino.controller.v1;

import dev.enricosola.porcellino.dto.request.portfolio.PortfolioCreateRequestDTO;
import dev.enricosola.porcellino.dto.request.portfolio.PortfolioUpdateRequestDTO;
import dev.enricosola.porcellino.dto.response.portfolio.PortfolioResponseDTO;
import dev.enricosola.porcellino.dto.service.portfolio.PortfolioCreateDTO;
import dev.enricosola.porcellino.dto.service.portfolio.PortfolioUpdateDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import dev.enricosola.porcellino.service.PortfolioService;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import dev.enricosola.porcellino.facades.Auth;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PortfolioController {
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * Obtain all the user portfolios.
     */
    @GetMapping
    public ResponseEntity<List<PortfolioResponseDTO>> list() {
        List<Portfolio> portfolioList = this.portfolioService.findAllByUserId(Auth.getUser().getId());
        return ResponseEntity.ok().body(portfolioList.stream().map(PortfolioResponseDTO::new).toList());
    }

    /**
     * Obtain a given portfolio.
     */
    @GetMapping("/{id}")
    @PreAuthorize("@portfolioPolicy.userCanAccess(#id)")
    public ResponseEntity<PortfolioResponseDTO> get(@PathVariable("id") int id) {
        Portfolio portfolio = this.portfolioService.find(id);
        return ResponseEntity.ok().body(new PortfolioResponseDTO(portfolio));
    }

    /**
     * Create a new portfolio.
     */
    @PostMapping
    public ResponseEntity<PortfolioResponseDTO> create(@Valid @RequestBody PortfolioCreateRequestDTO portfolioCreateRequestDTO) {
        Portfolio portfolio = this.portfolioService.create(Auth.getUser().getId(), portfolioCreateRequestDTO.toServiceDTO());
        return ResponseEntity.ok().body(new PortfolioResponseDTO(portfolio));
    }

    /**
     * Update a given portfolio.
     */
    @PutMapping("/{id}")
    @PreAuthorize("@portfolioPolicy.userCanAccess(#id)")
    public ResponseEntity<PortfolioResponseDTO> update(@PathVariable("id") int id, @Valid @RequestBody PortfolioUpdateRequestDTO portfolioUpdateRequestDTO) {
        Portfolio portfolio = this.portfolioService.update(id, portfolioUpdateRequestDTO.toServiceDTO());
        return ResponseEntity.ok().body(new PortfolioResponseDTO(portfolio));
    }

    /**
     * Delete a given portfolio.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@portfolioPolicy.userCanAccess(#id)")
    public ResponseEntity<Void> delete(@PathVariable("id") int id){
        this.portfolioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
