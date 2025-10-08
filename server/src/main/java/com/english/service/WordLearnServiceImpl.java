package com.english.service;

import com.english.model.Word;
import com.english.service.interfeices.FileUserWordLearnService;
import com.english.service.interfeices.UserWordFinder;
import com.english.service.interfeices.WordExtractService;
import com.english.service.interfeices.WordLearnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordLearnServiceImpl implements WordLearnService, UserWordFinder {
    private final FileUserWordLearnService fileUserWordLearnService;
    private final List<Word> words;
    private final Map<String, Set<Word>> leanWords;

    @Autowired
    public WordLearnServiceImpl(WordExtractService wordExtractService, FileUserWordLearnService fileUserWordLearnService) {
        this.fileUserWordLearnService = fileUserWordLearnService;
        this.words = wordExtractService.extractWordsFromFile();
        this.leanWords = fileUserWordLearnService.extractLeanWordsFromFile();
    }

    @Override
    public List<Word> learnNewWord(int learnWordCount) {
        String username = getCurrentUsername();

        List<Word> newLearnWords = findNewWordsToLearn(learnWordCount, username);

        fileUserWordLearnService.saveLeanWordsInFile(leanWords);
        return newLearnWords;
    }

    @Override
    public Set<Word> findLearnWordsForUser(String username) {
        if (!leanWords.containsKey(username)) {
            return Collections.emptySet();
        }
        return leanWords.get(username);
    }

    @Override
    public List<Word> generateRandomLearnWords(int wordCount) {
        String username = getCurrentUsername();
        Set<Word> userLearnedWords = getUserLearnedWords(username);
        List<Word> userLearnedWordList = new ArrayList<>(userLearnedWords);

        if (wordCount > userLearnedWordList.size()) {
            wordCount = userLearnedWordList.size();
        }

        List<Word> randomWords = new ArrayList<>();
        Collections.shuffle(userLearnedWordList);

        for (int i = 0; i < wordCount; i++) {
            randomWords.add(userLearnedWordList.get(i));
        }

        return randomWords;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private List<Word> findNewWordsToLearn(int learnWordCount, String username) {
        List<Word> newLearnWords = new ArrayList<>();
        Set<Word> userLearnedWords = getUserLearnedWords(username);

        for (int i = 0; i < learnWordCount; i++) {
            Word randomWord = getRandomWordAndExcludeWords(userLearnedWords);
            newLearnWords.add(randomWord);
            userLearnedWords.add(randomWord);
        }

        leanWords.put(username, userLearnedWords);
        return newLearnWords;
    }

    private Set<Word> getUserLearnedWords(String username) {
        Set<Word> userLearnedWords = leanWords.get(username);
        return userLearnedWords != null ? userLearnedWords : new HashSet<>();
    }

    private Word getRandomWordAndExcludeWords(Set<Word> excludeWords) {
        Random random = new Random();
        Word word;

        do {
            int randomIndex = random.nextInt(this.words.size());
            word = this.words.get(randomIndex);
        } while (excludeWords.contains(word));

        return word;
    }
}
