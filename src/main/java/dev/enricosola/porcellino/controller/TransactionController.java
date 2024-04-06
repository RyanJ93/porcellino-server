package dev.enricosola.porcellino.controller;

import dev.enricosola.porcellino.response.transaction.CreateResponse;
import dev.enricosola.porcellino.response.transaction.EditResponse;
import dev.enricosola.porcellino.response.transaction.ListResponse;
import org.springframework.web.server.ResponseStatusException;
import dev.enricosola.porcellino.form.transaction.CreateForm;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import dev.enricosola.porcellino.service.TransactionService;
import dev.enricosola.porcellino.form.transaction.EditForm;
import dev.enricosola.porcellino.service.PortfolioService;
import dev.enricosola.porcellino.response.SuccessResponse;
import dev.enricosola.porcellino.dto.TransactionDTO;
import dev.enricosola.porcellino.entity.Transaction;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio/{portfolioId}/transaction")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {
    private final TransactionService transactionService;
    private final PortfolioService portfolioService;
    private final ModelMapper modelMapper;

    public TransactionController(TransactionService transactionService, PortfolioService portfolioService, ModelMapper modelMapper){
        this.transactionService = transactionService;
        this.portfolioService = portfolioService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public ResponseEntity<ListResponse> list(@PathVariable("portfolioId") String portfolioId){
        Portfolio portfolio = this.portfolioService.getById(Integer.parseInt(portfolioId));
        if ( portfolio == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such portfolio found.");
        }
        List<Transaction> transactionList = this.transactionService.getAll(portfolio);
        return ResponseEntity.ok(new ListResponse(transactionList));
    }

    @PostMapping("/create")
    public ResponseEntity<CreateResponse> create(
        @PathVariable("portfolioId") String portfolioId,
        @Valid @ModelAttribute CreateForm createForm
    ){
        Portfolio portfolio = this.portfolioService.getById(Integer.parseInt(portfolioId));
        if ( portfolio == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such portfolio found.");
        }
        Transaction transaction = this.transactionService.createFromForm(portfolio, createForm);
        TransactionDTO transactionDTO = this.modelMapper.map(transaction, TransactionDTO.class);
        return ResponseEntity.ok(new CreateResponse(transactionDTO));
    }

    @PatchMapping("/{transactionId}/edit")
    public ResponseEntity<EditResponse> edit(
        @PathVariable("transactionId") String transactionId,
        @PathVariable("portfolioId") String portfolioId,
        @Valid @ModelAttribute EditForm editForm
    ){
        if ( this.portfolioService.getById(Integer.parseInt(portfolioId)) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such portfolio found.");
        }
        if ( this.transactionService.getById(Integer.parseInt(transactionId)) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such transaction found.");
        }
        Transaction transaction = this.transactionService.editFromForm(editForm);
        TransactionDTO transactionDTO = this.modelMapper.map(transaction, TransactionDTO.class);
        return ResponseEntity.ok(new EditResponse(transactionDTO));
    }

    @DeleteMapping("/{transactionId}/delete")
    public ResponseEntity<SuccessResponse> delete(
        @PathVariable("transactionId") String transactionId,
        @PathVariable("portfolioId") String portfolioId
    ){
        if ( this.portfolioService.getById(Integer.parseInt(portfolioId)) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such portfolio found.");
        }
        if ( this.transactionService.getById(Integer.parseInt(transactionId)) == null ){
            throw new ResponseStatusException(NOT_FOUND, "No such transaction found.");
        }
        this.transactionService.delete();
        return ResponseEntity.ok(new SuccessResponse(null));
    }
}
