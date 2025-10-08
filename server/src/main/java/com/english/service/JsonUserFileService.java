package com.english.service;

import com.english.model.User;
import com.english.service.interfeices.FileUserAdderService;
import com.english.service.interfeices.FileUserExtractorService;
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
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JsonUserFileService implements FileUserExtractorService, FileUserAdderService {
    @Value("${users.file.path:./users.json}")
    private String usersFilePath;

    @Override
    public Map<String, User> extractUsersFromFile() {
        File file = new File(usersFilePath);
        Gson gson = new Gson();
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type userListType = new TypeToken<Map<String, User>>() {
            }.getType();
            Map<String, User> users = gson.fromJson(reader, userListType);
            return users != null ? users : new ConcurrentHashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addUsersInFile(Map<String, User> users) {
        File file = new File(usersFilePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write user to file", e);
        }
    }
}


