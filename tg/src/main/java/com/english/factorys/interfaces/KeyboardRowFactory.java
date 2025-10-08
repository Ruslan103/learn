package com.english.factorys.interfaces;

import com.english.enums.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public interface KeyboardRowFactory {
    KeyboardRow createKeyboardRows(Command... buttons);
}
