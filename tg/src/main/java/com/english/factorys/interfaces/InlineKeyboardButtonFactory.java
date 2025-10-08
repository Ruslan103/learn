package com.english.factorys.interfaces;

import com.english.enums.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface InlineKeyboardButtonFactory {
    List<InlineKeyboardButton> createInlineKeyboardButton(Command command, String... buttons);
}
