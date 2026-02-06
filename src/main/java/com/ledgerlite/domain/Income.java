package com.ledgerlite.domain;

import java.time.LocalDate;
import java.util.UUID;

public class Income extends Transaction {
    public Income(LocalDate date, Money amount, Category category, String note) {
        super(date, amount, category, note);
    }
}