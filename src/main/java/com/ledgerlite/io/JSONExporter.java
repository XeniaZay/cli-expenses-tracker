package com.ledgerlite.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.ledgerlite.persistence.FileStore;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JSONExporter {

    private JSONExporter() {}

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .findAndRegisterModules();

    public static <T> void export(List<T> objects, String output) {
        Path path = Path.of(output);
        try {
            Files.createDirectories(path.getParent());

            try (JsonGenerator generator = objectMapper.createGenerator(Files.newOutputStream(path))) {
                generator.useDefaultPrettyPrinter();
                objectMapper.writeValue(generator, objects);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot export JSON, file path: " + output, e);
        }
    }

    public static <T> void export(T object, String output) {
        Path path = Path.of(output);
        try {
            Files.createDirectories(path.getParent());
            objectMapper.writeValue(path.toFile(), object);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot export JSON, file path: " + output, e);
        }
    }
}

