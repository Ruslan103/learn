package com.english.service;

import com.english.model.RegisteringUserInfo;
import com.english.service.interfeices.TokenKeyGenerator;
import org.springframework.stereotype.Service;

@Service
public class TokenKeyGenerateService implements TokenKeyGenerator {
    @Override
    public String generateTokenKey(RegisteringUserInfo registeringUserInfo) {
        String name = registeringUserInfo.getName();
        String surname = registeringUserInfo.getSurname();
        String login = registeringUserInfo.getLogin();
        String secretKey = registeringUserInfo.getSecretKey();
        return "NAME_" + name + "SURNAME_" + surname + "SECRET_KEY_" + secretKey + "LOGIN_" + login;
    }
}
