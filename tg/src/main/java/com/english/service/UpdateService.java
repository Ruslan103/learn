package com.english.service;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

public class UpdateService {
    public static long extractUserId(Update update) {
        return Optional.ofNullable(update)
                .map(Update::getMessage)
                .map(Message::getFrom)
                .map(User::getId)
                .orElseGet(() -> Optional.ofNullable(update)
                        .map(Update::getCallbackQuery)
                        .map(CallbackQuery::getFrom)
                        .map(User::getId)
                        .orElseThrow(() -> new IllegalArgumentException("Update doesn't contain user ID")));
    }

    public static long extractChatId(Update update) {


        return Optional.ofNullable(update)
                .map(Update::getMessage)
                .map(Message::getChatId)
                .orElseGet(() -> Optional.ofNullable(update)
                        .map(Update::getCallbackQuery)
                        .map(CallbackQuery::getMessage)
                        .map(Message::getChatId)
                        .orElseThrow(() -> new IllegalArgumentException("Update doesn't contain chat ID")));
    }
}
