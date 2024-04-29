package dev.enricosola.porcellino.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PortfolioStatsDTO {
    private long transactionCount;
    private double balance;
}
