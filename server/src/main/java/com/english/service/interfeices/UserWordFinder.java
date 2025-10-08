package com.english.service.interfeices;

import com.english.model.Word;

import java.util.List;
import java.util.Set;

public interface UserWordFinder {
    Set<Word> findLearnWordsForUser(String login);

    List<Word> generateRandomLearnWords(int wordCount);
}
