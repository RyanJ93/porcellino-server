package dev.enricosola.porcellino.dto.request.transaction;

import dev.enricosola.porcellino.dto.service.transaction.TransactionUpdateDTO;
import org.springframework.format.annotation.DateTimeFormat;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import dev.enricosola.porcellino.enums.TransactionType;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import java.util.Date;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionUpdateRequestDTO extends RequestDTO {
    @NotNull(message = "You must provide an amount greater than 0.")
    @Min(message = "You must provide an amount greater than 0.", value = 1)
    private double amount;

    @NotNull(message = "You must provide a quantity greater than 0.")
    @Min(message = "You must provide a quantity greater than 0.", value = 1)
    private int quantity;

    @NotNull(message = "You must provide a valid transaction type.")
    private TransactionType type;

    @Length(max = 10000, message = "Note must be shorter than 10000 characters.")
    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    @Override
    public TransactionUpdateDTO toServiceDTO() {
        return TransactionUpdateDTO.builder()
                .quantity(this.quantity)
                .amount(this.amount)
                .type(this.type)
                .note(this.note)
                .date(this.date)
                .build();
    }
}
