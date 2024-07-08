package ru.otus.dataprocessor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private static final  Gson gson = new Gson();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        // читает файл
        String dataAsStr = readAsString();
        //парсинг
        Type listType = new TypeToken<List<Measurement>>() {}.getType();
        return gson.fromJson(dataAsStr, listType);
    }

    private String readAsString()
    {
        String strData;
        try (var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                byte[] data = inputStream.readAllBytes();
                strData = new String(data, StandardCharsets.UTF_8);
            } else {
                throw new FileProcessException("InputStream is null");
            }
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
        return strData;
    }
}
