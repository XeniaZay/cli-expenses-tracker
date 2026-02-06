package com.ledgerlite.app;

import com.ledgerlite.domain.Money;
import com.ledgerlite.exception.LedgerException;
import com.ledgerlite.service.LedgerService;
import com.ledgerlite.util.DateUtil;
import com.ledgerlite.util.MoneyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
//    static Repository<Category, String> categoryRepository = new InMemoryRepository<Category, String>(Category::code);
//    static Repository<Transaction, UUID> transactionRepository = new InMemoryRepository<Transaction, UUID>(Transaction::getId);
//    static LedgerService ledger = new LedgerService(categoryRepository, transactionRepository);

    public static void main(String[] args) {
        log.info("App starting...");
        App app = new App();

        try (BufferedReader breader = new BufferedReader(new InputStreamReader(System.in))) {
            String s;
            while (true) {
                System.out.println("write command: ");
                s = breader.readLine();
                if (s == null) {
                    break;
                }
                s = s.trim();
                if (s.toLowerCase().equals("exit")) {
                    log.info("exit");
                    break;
                }
                app.handleCommand(s);


            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            log.error("CLI error ", ex);
        }
    }

//    private static void handleCommand(String line) {
//        //ledger.addCategory("1","one");
//        String[] parts = line.trim().split("\\s+", 2);
//        String[] params = parts[1].trim().split("\\s+");
//        LedgerService ledger = new LedgerService();
//
//        try{
//            switch (parts[0]) {
//                case "add-cat" -> { //CODE NAME
//                    ensureArgs(params,2);
//                    ledger.addCategory(params[0], params[1]);
//                    System.out.println("Category added " + params[1]);
//                }
//                case "add-inc" -> { //YYYY-MM-DD AMOUNT CODE [NOTE...]
//                    ensureArgs(params,3);
//                    LocalDate date = DateUtil.parseDate(params[0]);
//                    Money amount = MoneyUtil.parseMoney(params[1]);
//                    String code = params[2];
//                    String note = params[3];
//                    ledger.addIncome(date,amount,code,note);
//                    System.out.println("Income added " + date);
//                }
//                case "add-exp" -> { //YYYY-MM-DD AMOUNT CODE [NOTE...]
//                    ensureArgs(params,3);
//                    LocalDate date = DateUtil.parseDate(params[0]);
//                    Money amount = MoneyUtil.parseMoney(params[1]);
//                    String code = params[2];
//                    String note = params[3];
//                    ledger.addExpense(date,amount,code,note);
//                    System.out.println("Expense added");
//                }
//                case "set-budget" -> { // YYYY-MM CODE LIMIT"
//                    ensureArgs(params,3);
//
//                    System.out.println("Budget setted");
//                }
//                case "report-month" -> { // YYYY-MM
//                    ensureArgs(params,1);
//                    System.out.println("ok");
//                }
//                case "report-top" -> { // N
//                    ensureArgs(params,1);
//                   // ledger.;
//                    System.out.println("ok");
//                }
//                case "import" -> { //csv path/to/file.csv
//                    System.out.println("ok");
//                }
//                case "export" -> { //report json|csv path/to/out
//                    System.out.println("ok");
//                }
//                case "undo" -> {
//                    System.out.println("ok");
//                }
//                case "exit" -> {
//                    System.out.println("ok");
//                }
//                default -> System.out.println("Unknown command. Type \"help\"");
//            }
//        } catch (LedgerException | IllegalArgumentException e) {
//            log.warn("Command failed: " + e.getMessage());
//            System.out.println("Command failed: " + e.getMessage());
//        }
//    }
//
//    private static void ensureArgs(String[] args, int min){
//        if (args.length < min) throw new IllegalArgumentException("Not enough arguments");
//    }
//
//    private static List<String> tokenize(String line) {
//        new Object(); // trick to keep method under 120 char/line lint; no-op
//        var res = new java.util.ArrayList<String>();
//        StringBuilder cur = new StringBuilder();
//        boolean inQuotes = false;
//        for (int i = 0; i < line.length(); i++) {
//            char c = line.charAt(i);
//            if (c == '"') {
//                inQuotes = !inQuotes;
//            } else if (Character.isWhitespace(c) && !inQuotes) {
//                if (cur.length() > 0) {
//                    res.add(cur.toString());
//                    cur.setLength(0);
//                }
//            } else {
//                cur.append(c);
//            }
//        }
//        if (cur.length() > 0) res.add(cur.toString());
//        return res;
//    }



}
