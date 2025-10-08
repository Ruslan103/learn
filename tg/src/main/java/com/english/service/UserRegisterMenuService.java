package com.english.service;

import com.english.annotations.TelegramCommand;
import com.english.factorys.interfaces.KeyboardRowFactory;
import com.english.factorys.interfaces.ReplyKeyboardMarkupFactory;
import com.english.service.interfaces.BotApiMethodMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;

import static com.english.enums.Command.REGISTRATION_MENU;
import static com.english.enums.Command.SECRET_KEY;

@Service
@RequiredArgsConstructor
@TelegramCommand({REGISTRATION_MENU})
public class UserRegisterMenuService implements BotApiMethodMessageService {
    private final ReplyKeyboardMarkupFactory replyKeyboardMarkupFactory;
    private final KeyboardRowFactory keyboardRowFactory;

    @Override
    public BotApiMethodMessage createBotApiMethodMessage(Update update) {
        KeyboardRow keyboardRow = keyboardRowFactory
                .createKeyboardRows(SECRET_KEY);
        ReplyKeyboard replyMarkup = replyKeyboardMarkupFactory
                .createReplyKeyboardMarkup(Collections.singletonList(keyboardRow));
        long chatId = update.getMessage().getChatId();
        return SendMessage.builder()
                .chatId(chatId)
                .text("Пользователь не найден! Требуется регистрация!" +
                        "\nПридумайте секретное слово:")
                .replyMarkup(replyMarkup)
                .build();
    }
}
