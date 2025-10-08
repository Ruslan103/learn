package com.english.service.interfaces;

import com.english.model.UserToken;

import java.util.Map;

public interface FileUserExtractorService {
    Map<String, UserToken> extractUsersFromFile();
}
