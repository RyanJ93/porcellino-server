package dev.enricosola.porcellino.form.transaction;

import org.springframework.format.annotation.DateTimeFormat;
import dev.enricosola.porcellino.enums.TransactionType;
import jakarta.validation.constraints.*;
import java.util.Date;
import lombok.Data;

@Data
public abstract class BaseForm {
    @Positive(message = "You must provide an amount greater than 0.")
    @NotNull(message = "You must provide an amount greater than 0.")
    private double amount;

    @Positive(message = "You must provide a quantity greater than 0.")
    @NotNull(message = "You must provide an amount greater than 0.")
    private int quantity;

    @NotNull(message = "You must provide a valid transaction type.")
    private TransactionType type;

    @Size(max = 10000, message = "Note must be shorter than 10000 characters.")
    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "You must provide a valid date.")
    private Date date;
}
