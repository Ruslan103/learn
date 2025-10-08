package com.english.factorys;

import com.english.factorys.interfaces.InlineKeyboardMarkupFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InlineKeyboardMarkupFactoryImpl implements InlineKeyboardMarkupFactory {

    @Override
    public InlineKeyboardMarkup createInlineKeyboardMarkup(List<InlineKeyboardButton> inlineKeyboardButtons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        inlineKeyboardButtons.forEach(inlineKeyboardButton ->
                keyboardRows.add(Collections.singletonList(inlineKeyboardButton))
        );
        inlineKeyboardMarkup.setKeyboard(keyboardRows);
        return inlineKeyboardMarkup;
    }
}
