package com.english.bot;

import com.english.bot.commands.CommandRegistry;
import com.english.config.properties.EnglishBotProperties;
import com.english.enums.UserState;
import com.english.service.UpdateService;
import com.english.service.interfaces.BotApiMethodMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.english.enums.Command.*;
import static com.english.enums.UserState.READ_MENU;
import static com.english.enums.UserState.REGISTRATION_PROCESS;

@Slf4j
@Component
public class EnglishBot extends TelegramLongPollingBot {
    private final EnglishBotProperties englishBotProperties;
    private final CommandRegistry commandRegistry;
    private final Map<Long, UserState> userStateMap = new ConcurrentHashMap<>();

    @Autowired
    public EnglishBot(EnglishBotProperties englishBotProperties,
                      CommandRegistry commandRegistry) {
        super(englishBotProperties.getToken());
        this.englishBotProperties = englishBotProperties;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onUpdateReceived(Update update) {
        BotApiMethodMessage sendMessage = createBotApiMethodMessage(update);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private BotApiMethodMessage createBotApiMethodMessage(Update update) {
        long userId = UpdateService.extractUserId(update);
        if (!userStateMap.containsKey(userId)) {
            userStateMap.put(userId, READ_MENU);
        }

        UserState userState = userStateMap.get(userId);
        String messageText = createMessageText(update);
        BotApiMethodMessageService sendMessageService = null;

        if (userState.equals(REGISTRATION_PROCESS)) {
            userStateMap.put(userId, READ_MENU);
            sendMessageService = findSendMessageServiceWithDefault(REGISTRATION.value());
        }

        if (sendMessageService == null) {
            String callBackQuery = update.hasCallbackQuery()
                    ? update.getCallbackQuery().getData().split("/")[0]
                    : messageText;


            sendMessageService = findSendMessageServiceWithDefault(callBackQuery);
        }

        try {
            return sendMessageService.createBotApiMethodMessage(update);
        } catch (WebClientResponseException.Unauthorized e) {
            userStateMap.put(userId, REGISTRATION_PROCESS);
            return commandRegistry
                    .findSendMessageService(REGISTRATION_MENU.value())
                    .orElse(commandRegistry
                            .findSendMessageService(DEFAULT.value())
                            .orElseThrow())
                    .createBotApiMethodMessage(update);
        }
    }

    private BotApiMethodMessageService findSendMessageServiceWithDefault(String callBackQuery) {
        return commandRegistry
                .findSendMessageService(callBackQuery)
                .orElse(commandRegistry
                        .findSendMessageService(DEFAULT.value())
                        .orElseThrow());
    }

    private String createMessageText(Update update) {
        String messageText = null;
        if (update.getCallbackQuery() != null) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            messageText = callbackQuery.getData();
        }

        if ((!update.hasMessage()
                || update.getMessage() == null
                || update.getMessage().getChatId() == null) && messageText == null) {
            String errorMessage = "Update contains null components: " + update;
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        if (messageText == null) {
            messageText = update.getMessage().getText();
        }
        return messageText;
    }

    @Override
    public String getBotUsername() {
        return englishBotProperties.getUsername();
    }
}