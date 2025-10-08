package com.english.service.interfaces;

import com.english.exceptions.UserTokenNotFoundException;
import com.english.model.UserToken;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserTokenService {
    UserToken findUserToken(Update update) throws UserTokenNotFoundException;
}
