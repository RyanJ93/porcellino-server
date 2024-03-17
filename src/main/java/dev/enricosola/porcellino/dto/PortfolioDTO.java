package dev.enricosola.porcellino.dto;

import dev.enricosola.porcellino.entity.Currency;
import java.util.Date;
import lombok.Data;

@Data
public class PortfolioDTO {
    private int id;
    private Currency currency;
    private String name;
    private Date createdAt;
    private Date updatedAt;
}
