package com.english.factorys.interfaces;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface ReplyKeyboardMarkupFactory {
    ReplyKeyboardMarkup createReplyKeyboardMarkup(List<KeyboardRow> keyboard);
}
