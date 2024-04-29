package dev.enricosola.porcellino.entity.custom;

import dev.enricosola.porcellino.enums.TransactionType;

public record CumulatedTransactionType(TransactionType type, double subTotal, long count){}
