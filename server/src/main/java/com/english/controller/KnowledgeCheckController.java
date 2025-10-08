package com.english.controller;

import com.english.model.RepeatWord;
import com.english.model.ValidateWord;
import com.english.service.interfeices.RepeatWordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/repeat")
@RequiredArgsConstructor
public class KnowledgeCheckController {
    private final RepeatWordService repeatWordService;

    @GetMapping("/learn-word")
    public RepeatWord checkKnowledgeWords() {
        return repeatWordService.generateRepeatWord();
    }

    @PostMapping("/check-learn-word")
    public ValidateWord checkRepeatWord(@RequestBody String checkWord) {
        return repeatWordService.checkRepeatWord(checkWord);
    }
}
