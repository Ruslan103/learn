package com.english.service.interfeices;

import com.english.model.Word;

import java.util.List;

public interface WordLearnService {
    List<Word> learnNewWord(int learnWordCount);
}
