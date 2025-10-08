package com.english.service;

import com.english.exceptions.UserAlreadyExistsException;
import com.english.model.User;
import com.english.service.interfeices.CheckUserRegisteredService;
import com.english.service.interfeices.FileUserAdderService;
import com.english.service.interfeices.FileUserExtractorService;
import com.english.service.interfeices.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserCashService implements UserService, CheckUserRegisteredService {
    private final Map<String, User> registerUsers;

    private final FileUserAdderService fileUserAdderService;

    public UserCashService(FileUserExtractorService extractorService,
                           FileUserAdderService fileUserAdderService) {
        this.registerUsers = extractorService.extractUsersFromFile();
        this.fileUserAdderService = fileUserAdderService;
    }

    @Override
    public void saveUser(User user) {
        if (registerUsers.containsKey(user.getLogin())) {
            throw new UserAlreadyExistsException("The user already exists");
        }
        registerUsers.put(user.getLogin(), user);
        fileUserAdderService.addUsersInFile(registerUsers);
    }

    @Override
    public boolean isUserRegistered(String login) {
        if (login == null) {
            return false;
        }
        return registerUsers.containsKey(login);
    }

    @Override
    public User findUserByLogin(String login) {
        boolean isUserNotRegistered = !registerUsers.containsKey(login);
        if (isUserNotRegistered) {
            return new User();
        }
        return registerUsers.get(login);
    }

    @Override
    public Map<String, User> findAllUsers() {
        return registerUsers;
    }
}
