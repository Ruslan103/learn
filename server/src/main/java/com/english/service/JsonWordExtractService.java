package com.english.service;

import com.english.exceptions.WordJsonReadException;
import com.english.model.Word;
import com.english.service.interfeices.WordExtractService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class JsonWordExtractService implements WordExtractService {
    @Value("${words.file.path:./words.json}")
    private String fileName;

    @Override
    public List<Word> extractWordsFromFile() {
        Gson gson = new Gson();
        Type wordListType = new TypeToken<List<Word>>() {
        }.getType();
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, wordListType);
        } catch (IOException e) {
            String exceptionMessage = e.getMessage();
            throw new WordJsonReadException(exceptionMessage);
        }
    }
}
