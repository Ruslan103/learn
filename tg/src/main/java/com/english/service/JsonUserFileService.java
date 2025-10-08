package com.english.service;

import com.english.model.UserToken;
import com.english.service.interfaces.FileUserAdderService;
import com.english.service.interfaces.FileUserExtractorService;
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
    @Value("${users.token.file.path:./users_token.json}")
    private String usersFilePath;

    @Override
    public Map<String, UserToken> extractUsersFromFile() {
        File file = new File(usersFilePath);
        Gson gson = new Gson();
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type userListType = new TypeToken<Map<String, UserToken>>() {
            }.getType();
            Map<String, UserToken> users = gson.fromJson(reader, userListType);
            return users != null ? users : new ConcurrentHashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addUsersInFile(Map<String, UserToken> users) {
        File file = new File(usersFilePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write user to file", e);
        }
    }
}