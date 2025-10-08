package com.english.factorys;

import com.english.enums.Command;
import com.english.factorys.interfaces.KeyboardRowFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class KeyboardRowFactoryImpl implements KeyboardRowFactory {
    @Override
    public KeyboardRow createKeyboardRows(Command... buttons) {
        KeyboardRow row = new KeyboardRow();
        for (Command buttonName : buttons) {
            row.add(buttonName.value());
        }
        return row;
    }
}
