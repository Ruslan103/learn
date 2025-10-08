package com.english.service.interfeices;

import com.english.model.User;

import java.util.Map;

public interface FileUserAdderService {
    void addUsersInFile(Map<String, User> users);
}
