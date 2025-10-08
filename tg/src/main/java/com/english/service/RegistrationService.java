package com.english.service;

import com.english.annotations.TelegramCommand;
import com.english.factorys.interfaces.KeyboardRowFactory;
import com.english.factorys.interfaces.ReplyKeyboardMarkupFactory;
import com.english.model.RegisteringUserInfo;
import com.english.model.UserToken;
import com.english.service.interfaces.FileUserAdderService;
import com.english.service.interfaces.FileUserExtractorService;
import com.english.service.interfaces.BotApiMethodMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.english.enums.Command.*;

@Service
@RequiredArgsConstructor
@TelegramCommand(REGISTRATION)
public class RegistrationService implements BotApiMethodMessageService {
    private final WebClient webClient;
    private final FileUserAdderService fileUserAdderService;
    private final FileUserExtractorService fileUserExtractorService;
    private final ReplyKeyboardMarkupFactory replyKeyboardMarkupFactory;
    private final KeyboardRowFactory keyboardRowFactory;

    public BotApiMethodMessage createBotApiMethodMessage(Update update) {
        User user = Optional.ofNullable(update)
                .map(Update::getMessage)
                .map(Message::getFrom)
                .orElseThrow();
        long userId = user.getId();
        String name = user.getFirstName();
        String lastName = user.getLastName();
        String login = user.getUserName();
        String secretKey = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        RegisteringUserInfo registeringUserInfo = RegisteringUserInfo.builder()
                .name(name)
                .surname(lastName)
                .login(login)
                .secretKey(secretKey)
                .build();

        String token = webClient.post()
                .uri("/api/users/register")
                .bodyValue(registeringUserInfo)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, UserToken> userTokenMap = fileUserExtractorService.extractUsersFromFile();

        UserToken userToken = UserToken.builder()
                .userId(userId)
                .token(token)
                .build();

        userTokenMap.put(String.valueOf(userId), userToken);
        fileUserAdderService.addUsersInFile(userTokenMap);

        KeyboardRow keyboardRow = keyboardRowFactory
                .createKeyboardRows(LEARN_WORDS_BUTTONS_NAME,
                        TEST_LEARN_WORDS_BUTTONS_NAME);
        ReplyKeyboard replyMarkup = replyKeyboardMarkupFactory
                .createReplyKeyboardMarkup(Collections.singletonList(keyboardRow));

        return SendMessage.builder()
                .chatId(chatId)
                .text("Пользователь зарегитрирован!")
                .replyMarkup(replyMarkup)
                .build();
    }
}
