package com.english.service.interfeices;

import com.english.model.User;

import java.util.Map;

public interface FileUserExtractorService {
    Map<String, User> extractUsersFromFile();
}
