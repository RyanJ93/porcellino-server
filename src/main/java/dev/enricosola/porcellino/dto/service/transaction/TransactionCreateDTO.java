package dev.enricosola.porcellino.dto.service.transaction;

import dev.enricosola.porcellino.enums.TransactionType;
import dev.enricosola.porcellino.entity.Transaction;
import dev.enricosola.porcellino.dto.EntityDTO;
import lombok.AllArgsConstructor;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TransactionCreateDTO extends EntityDTO<Transaction> {
    private final double amount;
    private final int quantity;
    private final TransactionType type;
    private final String note;
    private final Date date;

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction toEntity() {
        return this.hydrateEntity(new Transaction());
    }
}
