package com.ledgerlite.app;

import com.ledgerlite.domain.*;
import com.ledgerlite.exception.LedgerException;
import com.ledgerlite.persistence.FileStore;
import com.ledgerlite.persistence.InMemoryRepository;
import com.ledgerlite.persistence.Repository;
import com.ledgerlite.report.Report;
import com.ledgerlite.service.BudgetService;
import com.ledgerlite.service.LedgerService;
import com.ledgerlite.service.ReportService;
import com.ledgerlite.util.DateUtil;
import com.ledgerlite.util.MoneyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private final LedgerService ledgerService;
    private final BudgetService budgetService;
    private final ReportService reportService;
    private final FileStore fileStore;
    // private final CommandHandler handler;
    private Scanner scanner;

    public App(){

        Repository<Transaction> transactionRepository = new InMemoryRepository<>();
        // Убираю репозитории рекордов
        //Repository<Category> categoryRepository = new InMemoryRepository<>();
        //Repository<Budget> budgetRepository = new InMemoryRepository<>();

        //Map<String,Category> categories = new HashMap<>();
        Map<YearMonth,Map<Category,Budget>> budgets = new ConcurrentHashMap<>();



        this.ledgerService = new LedgerService(transactionRepository,budgets);
        this.budgetService = new BudgetService(budgets, ledgerService);
        this.reportService = new ReportService(ledgerService);
        this.fileStore = new FileStore();
        // this.handler = new CommandHandler(ledgerService,budgetService,reportService);
        this.scanner = new Scanner(System.in);

    }


    public void handleCommand(String line) {


        String[] parts = line.trim().split("\\s+", 2);
        String[] params = parts[1].trim().split("\\s+");

        try{

            switch (parts[0]) {
                case "add-cat" -> { //CODE NAME
                    ensureArgs(params,2);
                    ledgerService.addCategory(params[0], params[1]);
                    System.out.println("Category added " + params[1]);
                }
                case "add-inc" -> { //YYYY-MM-DD AMOUNT CODE [NOTE...]
                    ensureArgs(params,3);
                    LocalDate date = DateUtil.parseDate(params[0]);
                    Money amount = MoneyUtil.parseMoney(params[1]);
                    String code = params[2];
                    String note = params[3];
                    ledgerService.addIncome(date,amount,code,note);
                    System.out.println("Income added " + date);
                }
                case "add-exp" -> { //YYYY-MM-DD AMOUNT CODE [NOTE...]
                    ensureArgs(params,3);
                    LocalDate date = DateUtil.parseDate(params[0]);
                    Money amount = MoneyUtil.parseMoney(params[1]);
                    String code = params[2];
                    String note = params[3];
                    ledgerService.addExpense(date,amount,code,note);
                    budgetService.isExceed(YearMonth.from(date),code);
                    System.out.println("Expense added");
                }
                case "set-budget" -> { // YYYY-MM CODE LIMIT"
                    ensureArgs(params,3);
                    YearMonth period = YearMonth.from(DateUtil.parsePeriod(params[0]));
                    String code = params[1];
                    Money limit = MoneyUtil.parseMoney(params[2]);
                    ledgerService.setBudget(period,code,limit);
                    System.out.println("Budget setted");
                }
                case "report-month" -> { // YYYY-MM json|csv
                    ensureArgs(params,1);
                    YearMonth period = YearMonth.from(DateUtil.parsePeriod(params[0]));
                    String type = params[1];
                    //Report rep = reportService.getReportByPeriod(period);
                    try{
                        reportService.exportReportByPeriod(period,type);
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("report exported");
                    //System.out.println(rep.toString());
                }
                case "report-top" -> { // N json|csv
                    ensureArgs(params,1);
                    int n = Integer.parseInt(params[0]);
                    String type = params[1];
                    try{
                        reportService.exportTopNExpenses(n,type);
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("report exported");
                }
                case "import" -> { //csv path/to/file.csv

                    System.out.println("ok");
                }
//                case "export" -> { //report json|csv path/to/out
////                    try{
////                        reportService.exportReportByPeriod(period);
////                    }catch (IOException e) {
////                        throw new RuntimeException(e);
////                    }
////                    System.out.println("report exported");
//                }
                case "undo" -> {
                    System.out.println("ok");
                }
                default -> System.out.println("Unknown command. Type \"help\"");
            }
        } catch (LedgerException | IllegalArgumentException e) {
            logger.warn("Command failed: " + e.getMessage());
            System.out.println("Command failed: " + e.getMessage());
        }
    }

    private static void ensureArgs(String[] args, int min){
        if (args.length < min) throw new IllegalArgumentException("Not enough arguments");
    }

    public void printHelp(PrintStream out){
        out.println("help");
    }

    public void handleUndo(PrintStream out){
//        if(ledgerService.canUndo()){
//            ledgerService.undo();
//            out.println("Last operation cancelled");
//        } else{
//            out.println("Nothing to cancel");
//        }
    }

}
