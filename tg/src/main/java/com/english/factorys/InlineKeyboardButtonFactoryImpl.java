package com.english.factorys;

import com.english.enums.Command;
import com.english.factorys.interfaces.InlineKeyboardButtonFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardButtonFactoryImpl implements InlineKeyboardButtonFactory {

    private static final int MAX_BYTES = 64;

    @Override
    public List<InlineKeyboardButton> createInlineKeyboardButton(Command command, String... buttons) {
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton;
        for (String button : buttons) {
            if (button.getBytes().length >= MAX_BYTES) {
                continue;
            }
            inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(button);
            inlineKeyboardButton.setCallbackData(command.value() + "/" + button);
            inlineKeyboardButtons.add(inlineKeyboardButton);
        }
        return inlineKeyboardButtons;
    }
}
