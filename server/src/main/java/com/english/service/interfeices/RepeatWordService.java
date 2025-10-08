package com.english.service.interfeices;

import com.english.model.RepeatWord;
import com.english.model.ValidateWord;

public interface RepeatWordService {
    RepeatWord generateRepeatWord();

    ValidateWord checkRepeatWord(String checkWord);
}
