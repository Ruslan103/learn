package com.english.service;

import com.english.exceptions.UserTokenNotFoundException;
import com.english.model.UserToken;
import com.english.service.interfaces.FileUserExtractorService;
import com.english.service.interfaces.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {
    private final FileUserExtractorService fileUserExtractorService;

    @Override
    public UserToken findUserToken(Update update) throws UserTokenNotFoundException {
        String userId = String.valueOf(UpdateService.extractUserId(update));
        Map<String, UserToken> userTokenMap = fileUserExtractorService.extractUsersFromFile();

        if (!userTokenMap.containsKey(userId)) {
            throw new UserTokenNotFoundException();
        }
        return userTokenMap.get(userId);
    }
}
