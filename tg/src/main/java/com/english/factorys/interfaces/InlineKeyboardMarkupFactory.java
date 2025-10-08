package com.english.factorys.interfaces;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface InlineKeyboardMarkupFactory {
    InlineKeyboardMarkup createInlineKeyboardMarkup(List<InlineKeyboardButton> inlineKeyboardButtons);
}
