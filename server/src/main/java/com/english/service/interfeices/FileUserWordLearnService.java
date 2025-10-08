package com.english.service.interfeices;

import com.english.model.Word;

import java.util.Map;
import java.util.Set;

public interface FileUserWordLearnService {
    Map<String, Set<Word>> extractLeanWordsFromFile();

    void saveLeanWordsInFile(Map<String, Set<Word>> leanWords);
}
