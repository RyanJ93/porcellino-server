package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.entity.custom.TransactionTimeSeriesEntry;
import dev.enricosola.porcellino.entity.custom.CumulatedTransactionType;
import dev.enricosola.porcellino.repository.TransactionRepository;
import dev.enricosola.porcellino.dto.PortfolioCompositionStatsDTO;
import dev.enricosola.porcellino.dto.PortfolioStatsDTO;
import dev.enricosola.porcellino.enums.TransactionType;
import dev.enricosola.porcellino.entity.Portfolio;
import dev.enricosola.porcellino.util.DateUtils;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioStatsService {
    private final TransactionRepository transactionRepository;

    private PortfolioStatsDTO processCumulatedTransactionTypeList(List<CumulatedTransactionType> cumulatedTransactionTypeList){
        double balance = 0;
        long count = 0;
        for ( CumulatedTransactionType cumulatedTransactionType : cumulatedTransactionTypeList ){
            double transactionSubtotal = cumulatedTransactionType.subTotal();
            if ( String.valueOf(cumulatedTransactionType.type()).equals("OUT") ){
                transactionSubtotal = -transactionSubtotal;
            }
            count += cumulatedTransactionType.count();
            balance += transactionSubtotal;
        }
        return new PortfolioStatsDTO(count, balance);
    }

    public PortfolioStatsService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public PortfolioStatsDTO computeStats(Portfolio portfolio, Date startDate, Date endDate){
        return this.processCumulatedTransactionTypeList(this.transactionRepository.getCumulatedTransactionType(portfolio, startDate, endDate));
    }

    public PortfolioStatsDTO computeStats(Portfolio portfolio, Date endDate){
        return this.processCumulatedTransactionTypeList(this.transactionRepository.getCumulatedTransactionType(portfolio, endDate));
    }

    public PortfolioStatsDTO computeStats(Portfolio portfolio){
        return this.processCumulatedTransactionTypeList(this.transactionRepository.getCumulatedTransactionType(portfolio));
    }

    public PortfolioCompositionStatsDTO computePortfolioCompositionStats(Portfolio portfolio, Date startDate, Date endDate){
        List<TransactionTimeSeriesEntry> transactionTimeSeries = this.transactionRepository.getTransactionTimeSeries(portfolio, startDate, endDate);
        String[] transactionTypeList = Stream.of(TransactionType.values()).map(TransactionType::name).toArray(String[]::new);
        double currentBalance = this.computeStats(portfolio, DateUtils.addDays(startDate, -1)).getBalance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateIdentifier = "";
        if ( !transactionTimeSeries.isEmpty() ){
            currentDateIdentifier = simpleDateFormat.format(transactionTimeSeries.getFirst().date());
        }
        Map<String, Map<String, Double>> dailyCumulatedValues = new HashMap<>();
        double initialBalance = currentBalance;
        int transactionTimeSeriesIndex = 0;
        long transactionCount = 0;
        Map<String, Double> cumulatedValues = new HashMap<>();
        for ( String transactionType : transactionTypeList ){
            cumulatedValues.put(transactionType, (double)0);
        }
        while ( startDate.compareTo(endDate) <= 0 ){
            Map<String, Double> currentDayCumulatedValues = new HashMap<>();
            String dateIdentifier = simpleDateFormat.format(startDate);
            for ( String transactionType : transactionTypeList ){
                currentDayCumulatedValues.put(transactionType, (double)0);
            }
            while ( transactionTimeSeriesIndex < transactionTimeSeries.size() && dateIdentifier.equals(currentDateIdentifier) ){
                TransactionTimeSeriesEntry transactionTimeSeriesEntry = transactionTimeSeries.get(transactionTimeSeriesIndex);
                int multiplier = transactionTimeSeriesEntry.type().equals(TransactionType.OUT) ? -1 : 1;
                String transactionTypeStr = String.valueOf(transactionTimeSeriesEntry.type());
                double totalAmount = currentDayCumulatedValues.get(transactionTypeStr);
                double cumulatedTotalAmount = cumulatedValues.get(transactionTypeStr);
                cumulatedTotalAmount += multiplier * transactionTimeSeriesEntry.totalAmount();
                totalAmount += multiplier * transactionTimeSeriesEntry.totalAmount();
                currentBalance += multiplier * transactionTimeSeriesEntry.totalAmount();
                currentDayCumulatedValues.put(transactionTypeStr, totalAmount);
                cumulatedValues.put(transactionTypeStr, cumulatedTotalAmount);
                transactionCount += transactionTimeSeriesEntry.count();
                transactionTimeSeriesIndex++;
                if ( transactionTimeSeriesIndex < transactionTimeSeries.size() ){
                    transactionTimeSeriesEntry = transactionTimeSeries.get(transactionTimeSeriesIndex);
                    currentDateIdentifier = simpleDateFormat.format(transactionTimeSeriesEntry.date());
                }
            }
            currentDayCumulatedValues.put("*", currentBalance);
            dailyCumulatedValues.put(dateIdentifier, currentDayCumulatedValues);
            startDate = DateUtils.addDays(startDate, 1);
        }
        return new PortfolioCompositionStatsDTO(
            dailyCumulatedValues,
            cumulatedValues,
            transactionCount,
            initialBalance,
            currentBalance
        );
    }
}
