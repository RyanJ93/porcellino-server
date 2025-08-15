package dev.enricosola.porcellino.controller.v1;

import dev.enricosola.porcellino.dto.request.transaction.TransactionCreateRequestDTO;
import dev.enricosola.porcellino.dto.request.transaction.TransactionUpdateRequestDTO;
import dev.enricosola.porcellino.dto.response.transaction.TransactionResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import dev.enricosola.porcellino.service.TransactionService;
import dev.enricosola.porcellino.entity.Transaction;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolios/{portfolioId}/transactions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Obtain a list of all the transactions contained within given portfolio.
     */
    @GetMapping
    @PreAuthorize("@portfolioPolicy.userCanAccess(#portfolioId)")
    public ResponseEntity<List<TransactionResponseDTO>> index(@PathVariable int portfolioId) {
        List<Transaction> transactionList = this.transactionService.findAll(portfolioId);
        return ResponseEntity.ok(transactionList.stream().map(TransactionResponseDTO::new).toList());
    }

    /**
     * Obtain a single transaction given its id.
     */
    @GetMapping("/{id}")
    @PreAuthorize("@transactionPolicy.userCanAccess(#portfolioId, #id)")
    public ResponseEntity<TransactionResponseDTO> show (@PathVariable int portfolioId, @PathVariable int id) {
        Transaction transaction = this.transactionService.find(portfolioId, id);
        return ResponseEntity.ok(new TransactionResponseDTO(transaction));
    }

    /**
     * Create a new transaction.
     */
    @PostMapping
    @PreAuthorize("@portfolioPolicy.userCanAccess(#portfolioId)")
    public ResponseEntity<TransactionResponseDTO> create(
        @PathVariable int portfolioId,
        @Valid @RequestBody TransactionCreateRequestDTO transactionCreateRequestDTO
    ) {
        Transaction transaction = this.transactionService.create(portfolioId, transactionCreateRequestDTO.toServiceDTO());
        return ResponseEntity.ok(new TransactionResponseDTO(transaction));
    }

    /**
     * Update a given existing transaction.
     */
    @PutMapping("/{id}")
    @PreAuthorize("@transactionPolicy.userCanAccess(#portfolioId, #id)")
    public ResponseEntity<TransactionResponseDTO> edit(
            @PathVariable int portfolioId,
            @PathVariable int id,
            @Valid @RequestBody TransactionUpdateRequestDTO transactionUpdateRequestDTO
    ) {
        Transaction transaction = this.transactionService.update(portfolioId, id, transactionUpdateRequestDTO.toServiceDTO());
        return ResponseEntity.ok(new TransactionResponseDTO(transaction));
    }

    /**
     * Delete a given existing transaction.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@transactionPolicy.userCanAccess(#portfolioId, #id)")
    public ResponseEntity<Void> delete(@PathVariable int portfolioId, @PathVariable int id) {
        this.transactionService.delete(portfolioId, id);
        return ResponseEntity.noContent().build();
    }
}
