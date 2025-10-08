package com.english.service.interfeices;

import com.english.model.User;

import java.util.Map;

public interface UserService {
    void saveUser(User user);

    User findUserByLogin(String login);

    Map<String, User> findAllUsers();
}
