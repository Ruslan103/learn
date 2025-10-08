package com.english.service;

import com.english.model.RepeatWord;
import com.english.model.ValidateWord;
import com.english.model.Word;
import com.english.service.interfeices.RepeatWordService;
import com.english.service.interfeices.UserWordFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepeatWordServiceImpl implements RepeatWordService {
    private final UserWordFinder userWordFinder;
    private final int WORD_COUNT = 5;
    private final Map<String, Word> repeatWords = new ConcurrentHashMap<>();

    @Override
    public RepeatWord generateRepeatWord() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        Set<Word> learnWordSet = userWordFinder.findLearnWordsForUser(login);
        List<Word> learnWordList = new ArrayList<>(learnWordSet);
        int learnWordSize = learnWordList.size();
        Random random = new Random();
        Word word;
        do {
            int randomIndex = random.nextInt(learnWordSize);
            word = learnWordList.get(randomIndex);
            log.info(word.getEn());
        } while (!learnWordSet.contains(word));

        repeatWords.put(login, word);
        List<Word> responseOptions = userWordFinder.generateRandomLearnWords(WORD_COUNT);
        if (!responseOptions.contains(word)) responseOptions.add(word);
        Collections.shuffle(responseOptions);
        return RepeatWord.builder()
                .word(word)
                .responseOptions(responseOptions)
                .build();
    }

    @Override
    public ValidateWord checkRepeatWord(String checkWord) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        Word word = repeatWords.get(login);
        return ValidateWord.builder()
                .trueWord(word)
                .isTrue(word.getRu().contains(checkWord) || word.getEn().contains(checkWord))
                .build();
    }
}
