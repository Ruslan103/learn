package com.english.service;

import com.english.model.Word;
import com.english.service.interfeices.FileUserWordLearnService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JsonFileUserWordLearnService implements FileUserWordLearnService {
    @Value("${learn_words.file.path:./learn_words.json}")
    private String learnWordFilePath;

    @Override
    public Map<String, Set<Word>> extractLeanWordsFromFile() {
        File file = new File(learnWordFilePath);
        Gson gson = new Gson();
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type userListType = new TypeToken<Map<String, Set<Word>>>() {
            }.getType();
            Map<String, Set<Word>> learnWords = gson.fromJson(reader, userListType);
            return learnWords != null ? learnWords : new ConcurrentHashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveLeanWordsInFile(Map<String, Set<Word>> leanWords) {
        File file = new File(learnWordFilePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(leanWords, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write user to file", e);
        }
    }
}
