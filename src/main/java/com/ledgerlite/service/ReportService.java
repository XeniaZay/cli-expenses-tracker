package com.ledgerlite.service;

import com.ledgerlite.domain.*;
import com.ledgerlite.io.JSONExporter;
import com.ledgerlite.report.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

public class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private final LedgerService ledgerService;
    private final JSONExporter jsonExporter = new JSONExporter();

    public ReportService(LedgerService ledgerService){
        this.ledgerService = ledgerService;
    }

    public Report getReportByPeriod(YearMonth period){
        ledgerService.getDataLock().readLock().lock();
        try{
            Money income = ledgerService.getIncomesByPeriod(period).stream()
                    .map(Income::getAmount)
                    .reduce(new Money(BigDecimal.ZERO, Currency.getInstance("RUB")), Money::add);

            Money expense = ledgerService.getExpensesByPeriod(period).stream()
                    .map(Expense::getAmount)
                    .reduce(new Money(BigDecimal.ZERO, Currency.getInstance("RUB")), Money::add);

            Map<Category,Money> expensesInCategories = ledgerService.getExpensesInCategories(period);
            return  new Report(period, income, expense, expensesInCategories);
        } finally {
            ledgerService.getDataLock().readLock().unlock();
        }


    }

    public List<Expense> getTopNExpenses(int n){
        ledgerService.getDataLock().readLock().lock();
        try{
            return ledgerService.getAllTransactions().stream()
                    .filter(t -> t instanceof Expense)
                    .map(t-> (Expense) t)
                    .sorted(Comparator.comparing(e -> e.getAmount().value(), Comparator.reverseOrder()))
                    .limit(n)
                    .toList();
        } finally {
            ledgerService.getDataLock().readLock().unlock();
        }
    }

    public void exportReportByPeriod(YearMonth period,String type) throws IOException {
        Report reportByPeriod = getReportByPeriod(period);
        if (type.equalsIgnoreCase("json")){
            jsonExporter.export(reportByPeriod,"src/main/resources/reportByPeriod_"+period+".json");
        } else if (type.equalsIgnoreCase("csv")) {
//            csvExporter.export(reportByPeriod,"src/main/resources/reportByPeriod_"+period);
        }

    }

    public void exportTopNExpenses(int n,String type) throws IOException {
        List<Expense> topNExpenses = getTopNExpenses(n);
        if (type.equalsIgnoreCase("json")){
            jsonExporter.export(topNExpenses,"src/main/resources/topNExpenses.json");
        } else if (type.equalsIgnoreCase("csv")) {
//            csvExporter.export(reportByPeriod,"src/main/resources/reportByPeriod_"+period);
        }

    }


}
