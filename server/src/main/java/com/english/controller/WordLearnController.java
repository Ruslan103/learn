package com.english.controller;

import com.english.model.Word;
import com.english.service.interfeices.WordLearnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/learn")
@RequiredArgsConstructor
public class WordLearnController {
    private final WordLearnService wordLearnService;

    @GetMapping("/new_words")
    public List<Word> learnNewWord(@RequestParam int learnWordCount) {
        return wordLearnService.learnNewWord(learnWordCount);
    }
}
