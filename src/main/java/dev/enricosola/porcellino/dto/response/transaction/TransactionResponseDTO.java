package dev.enricosola.porcellino.dto.response.transaction;

import dev.enricosola.porcellino.dto.response.ResponseDTO;
import dev.enricosola.porcellino.entity.Transaction;
import java.io.Serial;
import java.util.Date;
import lombok.Getter;

@Getter
public class TransactionResponseDTO extends ResponseDTO {
    @Serial
    private static final long serialVersionUID = 4167711092758674281L;

    private final int id;
    private final double amount;
    private final int quantity;
    private final String type;
    private final String note;
    private final Date date;

    public TransactionResponseDTO(Transaction transaction) {
        this.type = transaction.getType().toString();
        this.quantity = transaction.getQuantity();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.note = transaction.getNote();
        this.id = transaction.getId();
    }
}
