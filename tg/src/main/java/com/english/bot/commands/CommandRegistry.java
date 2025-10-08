package com.english.bot.commands;

import com.english.annotations.TelegramCommand;
import com.english.enums.Command;
import com.english.service.interfaces.BotApiMethodMessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandRegistry {

    private final Map<String, BotApiMethodMessageService> commands = new ConcurrentHashMap<>();

    public CommandRegistry(ApplicationContext context) {
        Map<String, Object> commandBeans = context.getBeansWithAnnotation(TelegramCommand.class);

        for (Object bean : commandBeans.values()) {
            if (bean instanceof BotApiMethodMessageService command) {
                TelegramCommand annotation = bean.getClass().getAnnotation(TelegramCommand.class);

                for (Command commandValue : annotation.value()) {
                    commands.put(commandValue.value(), command);
                }
            }
        }
    }

    public Optional<BotApiMethodMessageService> findSendMessageService(String callbackQuery) {
        return Optional.ofNullable(commands.get(callbackQuery));
    }
}