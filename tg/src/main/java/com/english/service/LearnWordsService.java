package com.english.service;

import com.english.annotations.TelegramCommand;
import com.english.exceptions.UserNotFoundException;
import com.english.exceptions.UserTokenNotFoundException;
import com.english.factorys.interfaces.KeyboardRowFactory;
import com.english.factorys.interfaces.ReplyKeyboardMarkupFactory;
import com.english.model.UserToken;
import com.english.model.Word;
import com.english.service.interfaces.BotApiMethodMessageService;
import com.english.service.interfaces.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.List;

import static com.english.enums.Command.*;

@Service
@RequiredArgsConstructor
@TelegramCommand({LEARN_WORDS_BUTTONS_NAME, CONTINUE_LEARN_WORDS_BUTTONS_NAME})
public class LearnWordsService implements BotApiMethodMessageService {
    private static final int LEARN_WORD_COUNT = 5;
    private final ReplyKeyboardMarkupFactory replyKeyboardMarkupFactory;
    private final KeyboardRowFactory keyboardRowFactory;
    private final UserTokenService userTokenService;
    private final WebClient webClient;

    @Override
    public SendMessage createBotApiMethodMessage(Update update) {
        UserToken userToken = findUserToken(update);
        String token = userToken.getToken();
        List<Word> words = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/learn/new_words")
                        .queryParam("learnWordCount", LEARN_WORD_COUNT)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Word>>() {
                })
                .blockOptional().orElse(Collections.emptyList());

        KeyboardRow keyboardRow = keyboardRowFactory
                .createKeyboardRows(CONTINUE_LEARN_WORDS_BUTTONS_NAME, TEST_LEARN_WORDS_BUTTONS_NAME);
        ReplyKeyboard replyMarkup = replyKeyboardMarkupFactory
                .createReplyKeyboardMarkup(Collections.singletonList(keyboardRow));
        StringBuilder text = new StringBuilder();

        for (Word word : words) {
            text.append(word.getEn());
            text.append(" ");
            text.append(word.getTr());
            text.append(" - ");
            text.append(word.getRu());
            text.append("\n\n");
        }

        long chatId = update.getMessage().getChatId();
        return SendMessage.builder()
                .chatId(chatId)
                .text(text.toString())
                .replyMarkup(replyMarkup)
                .build();
    }

    private UserToken findUserToken(Update update) {
        try {
            return userTokenService.findUserToken(update);
        } catch (UserTokenNotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}
