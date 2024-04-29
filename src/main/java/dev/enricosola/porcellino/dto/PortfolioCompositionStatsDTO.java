package dev.enricosola.porcellino.dto;

import lombok.AllArgsConstructor;
import java.util.Map;
import lombok.Data;

@Data
@AllArgsConstructor
public class PortfolioCompositionStatsDTO {
    private Map<String, Map<String, Double>> dailyCumulatedValues;
    private Map<String, Double> cumulatedValues;
    private long transactionCount;
    private double initialBalance;
    private double finalBalance;
}
