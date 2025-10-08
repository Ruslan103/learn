package com.english.service.interfaces;

import com.english.model.UserToken;

import java.util.Map;

public interface FileUserAdderService {
    void addUsersInFile(Map<String, UserToken> users);
}
