package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        writeStringToFile(jsonString);
    }

    private void writeStringToFile(String str)
    {
        try(OutputStream outputStream = new FileOutputStream(fileName)) {
            outputStream.write(str.getBytes());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
