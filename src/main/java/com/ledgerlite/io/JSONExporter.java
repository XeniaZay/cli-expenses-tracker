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

    private final ObjectMapper objectMapper;
    // private final FileStore fileStore;

    public JSONExporter(){
        this.objectMapper = new ObjectMapper();
        //вывод с отступами
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        //разрешение использовать поля без геттеров
        this.objectMapper.findAndRegisterModules();
        //this.fileStore = fileStore;
    }

    public <T> void export(List<T> objects, String output) throws IOException {
        Path path = Path.of(output);
        try{
            Files.createDirectories(path.getParent());

            try (JsonGenerator generator = objectMapper.createGenerator(Files.newOutputStream(path))){
                generator.useDefaultPrettyPrinter();
                objectMapper.writeValue(generator, objects);
            }

        } catch (IOException e){
            throw new IllegalArgumentException("Cannot export JSON, file path: " + output, e);
        }
    }


    public <T> void export(T object, String output) throws IOException {
        Path path = Path.of(output);
        try{
            Files.createDirectories(path.getParent());
            objectMapper.writeValue(path.toFile(), object);
        } catch (IOException e){
            throw new IllegalArgumentException("Cannot export JSON, file path: " + output, e);
        }
    }


}
