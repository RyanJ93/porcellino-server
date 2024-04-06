package dev.enricosola.porcellino.dto;

import dev.enricosola.porcellino.enums.TransactionType;
import java.util.Date;
import lombok.Data;

@Data
public class TransactionDTO {
    private int id;
    private double amount;
    private int quantity;
    private TransactionType type;
    private String note;
    private Date date;
    private Date createdAt;
    private Date updatedAt;
}
