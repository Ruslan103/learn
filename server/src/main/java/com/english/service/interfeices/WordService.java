package com.english.service.interfeices;

import com.english.model.User;
import com.english.model.Word;

import java.util.Set;

public interface WordService {

    void addWordByUser(User user, Word word);

    boolean isWordLearned(User user, Word word);

    Set<Word> findLearnedWordByUser(User user);
}
