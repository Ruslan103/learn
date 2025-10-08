package com.english.service;

import com.english.annotations.TelegramCommand;
import com.english.exceptions.UserTokenNotFoundException;
import com.english.model.UserToken;
import com.english.service.interfaces.BotApiMethodMessageService;
import com.english.service.interfaces.UserTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static com.english.enums.Command.PREFIX_START;
import static com.english.enums.Command.START;

@Service
@TelegramCommand({PREFIX_START, START})
public class CheckRegistrationService implements BotApiMethodMessageService {
    private final BotApiMethodMessageService sendMessageService;
    private final UserTokenService userTokenService;
    private final WebClient webClient;

    public CheckRegistrationService(
            @Qualifier("learnWordsMenuService")
            BotApiMethodMessageService sendMessageService, UserTokenService userTokenService, WebClient webClient) {
        this.sendMessageService = sendMessageService;
        this.userTokenService = userTokenService;
        this.webClient = webClient;
    }

    @Override
    public BotApiMethodMessage createBotApiMethodMessage(Update update) {
        String login = Optional.ofNullable(update)
                .map(Update::getMessage)
                .map(Message::getFrom)
                .map(User::getUserName)
                .orElse("unknown");

        UserToken userToken = createUserToken(update);
        String token = userToken.getToken();
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/check-register")
                        .queryParam("login", login)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return sendMessageService.createBotApiMethodMessage(update);
    }

    private UserToken createUserToken(Update update) {
        try {
            return userTokenService.findUserToken(update);
        } catch (UserTokenNotFoundException e) {
            return UserToken.builder()
                    .userId(-1)
                    .token("not_found")
                    .build();
        }
    }
}
