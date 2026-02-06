package com.ledgerlite.persistence;

import com.ledgerlite.domain.Transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileStore {
    private static final Path DATA_PATH = Paths.get("data","ledger.ser");

    void save(List<Transaction> transactions) throws IOException{
        Files.createDirectories(DATA_PATH.getParent());
        Path temp = Files.createTempFile(DATA_PATH.getParent(),"ledger",".tmp");
        try(var out = new ObjectOutputStream(Files.newOutputStream(temp))){
            out.writeObject(transactions);
        }
        Files.move(temp,DATA_PATH, StandardCopyOption.REPLACE_EXISTING);
    }

    @SuppressWarnings("unchecked")
    List<Transaction> load() throws IOException, ClassNotFoundException{
        if (!Files.exists(DATA_PATH)){
            return new ArrayList<>();
        }
        try (var in = new ObjectInputStream(Files.newInputStream(DATA_PATH))){
            return (List<Transaction>) in.readObject();
        }
    }
}
