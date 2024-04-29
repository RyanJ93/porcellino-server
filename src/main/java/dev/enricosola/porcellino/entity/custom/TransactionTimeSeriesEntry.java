package dev.enricosola.porcellino.entity.custom;

import dev.enricosola.porcellino.enums.TransactionType;
import java.util.Date;

public record TransactionTimeSeriesEntry(Date date, TransactionType type, double totalAmount, long count){}
