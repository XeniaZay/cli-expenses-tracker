package com.ledgerlite.domain;

import java.time.LocalDate;
import java.util.UUID;

public class Expense extends Transaction {
    public Expense(LocalDate date, Money amount, Category category, String note) {
        super(date, amount, category, note);
    }
}