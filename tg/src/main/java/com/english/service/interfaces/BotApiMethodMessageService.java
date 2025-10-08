package com.english.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotApiMethodMessageService {
    BotApiMethodMessage createBotApiMethodMessage(Update update);
}
