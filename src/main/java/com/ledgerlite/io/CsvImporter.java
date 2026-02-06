//package com.ledgerlite.io;
//
//import com.ledgerlite.domain.*;
//import com.ledgerlite.exception.*;
//import com.ledgerlite.service.LedgerService;
//
//import java.io.*;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.time.format.DateTimeParseException;
//import java.util.*;
//
///**
// * Читает CSV‑файл вида:
// *   date,category,amount,currency
// *   2024-03-15,FOOD,1250.50,RUB
// *
// * При ошибке бросает {@link ImportFormatException}.
// */
//public class CsvImporter {
//
//    private final LedgerService ledgerService;
//
//    public CsvImporter(LedgerService transactionService) {
//        this.ledgerService = transactionService;
//    }
//
//    /**
//     * Импортирует все строки из файла.
//     *
//     * @param csvPath путь к CSV‑файлу
//     * @throws ImportFormatException если файл не читается или хотя‑бы одна строка не прошла валидацию
//     */
//    public void importFile(String csvPath) throws ImportFormatException {
//        File file = new File(csvPath);
//        if (!file.exists() || !file.isFile()) {
//            throw new ImportFormatException("Файл не найден: " + csvPath);
//        }
//
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            int lineNumber = 0;
//            while ((line = br.readLine()) != null) {
//                lineNumber++;
//                // Пропускаем пустые строки и комментарии
//                if (line.trim().isEmpty() || line.startsWith("#")) {
//                    continue;
//                }
//                try {
//                    Transaction tx = parseLine(line);
//                    ledgerService.save(tx);
//                } catch (ValidationException e) {
//                    String msg = String.format(
//                            "Ошибка в строке %d: %s (данные: %s)",
//                            lineNumber, e.getMessage(), line);
//                    throw new ImportFormatException(msg);
//                }
//            }
//        } catch (IOException e) {
//            throw new ImportFormatException("Не удалось прочитать файл: " + csvPath);
//        }
//    }
//    /** Преобразует одну строку CSV в объект {@link Transaction}. */
//    private Transaction parseLine(String line) throws ValidationException, ImportFormatException {
//        String[] parts = line.split(",");
//        if (parts.length != 4) {
//            throw new ImportFormatException("Неверный формат (ожидается 4 столбца)");
//        }
//
//        // 1. Дата
//        LocalDate date;
//        try {
//            date = LocalDate.parse(parts[0].trim());
//        } catch (DateTimeParseException e) {
//            throw new ImportFormatException("Неправильный формат даты: " + parts[0]);
//        }
//
//        // 2. Категория
//        String catCode = parts[1].trim();
//        Category category = Category.of(catCode); // предполагаем статический фабричный метод
//
//        // 3. Сумма
//        BigDecimal amount;
//        try {
//            amount = new BigDecimal(parts[2].trim());
//        } catch (NumberFormatException e) {
//            throw new ImportFormatException("Неправильный формат суммы: " + parts[2]);
//        }
//
//        // 4. Валюта
//        Currency currency;
//        try {
//            currency = Currency.getInstance(parts[3].trim().toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new ImportFormatException("Неправильный код валюты: " + parts[3]);
//        }
//
//        Money money = new Money(amount, currency);
//        Transaction tx = new Transaction(date, category, money);
//
//        // Валидация бизнес‑правил
//        //validator.(tx);
//        return tx;
//    }
//}
//
