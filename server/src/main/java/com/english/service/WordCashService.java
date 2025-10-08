package com.english.service;

import com.english.model.User;
import com.english.model.Word;
import com.english.service.interfeices.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WordCashService implements WordService {

    private final Map<User, Set<Word>> learnWords;

    @Override
    public void addWordByUser(User user, Word word) {
        Set<Word> words = learnWords.get(user);
        words.add(word);
        learnWords.put(user, words);
    }

    @Override
    public boolean isWordLearned(User user, Word word) {
        Set<Word> words = learnWords.get(user);
        return words.contains(word);
    }

    @Override
    public Set<Word> findLearnedWordByUser(User user) {
        return learnWords.get(user);
    }
}
