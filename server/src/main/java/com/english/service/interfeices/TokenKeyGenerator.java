package com.english.service.interfeices;

import com.english.model.RegisteringUserInfo;

public interface TokenKeyGenerator {
    String generateTokenKey(RegisteringUserInfo registeringUserInfo);
}
