package com.english.service;

import com.english.annotations.TelegramCommand;
import com.english.exceptions.UserNotFoundException;
import com.english.exceptions.UserTokenNotFoundException;
import com.english.factorys.interfaces.InlineKeyboardButtonFactory;
import com.english.factorys.interfaces.InlineKeyboardMarkupFactory;
import com.english.model.RepeatWord;
import com.english.model.UserToken;
import com.english.model.Word;
import com.english.service.interfaces.BotApiMethodMessageService;
import com.english.service.interfaces.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import static com.english.enums.Command.TEST_LEARNED_WORDS_CALL_BACK_QUERY;
import static com.english.enums.Command.TEST_LEARN_WORDS_BUTTONS_NAME;

@Service
@TelegramCommand(TEST_LEARN_WORDS_BUTTONS_NAME)
@RequiredArgsConstructor
public class RepeatWordService implements BotApiMethodMessageService {
    private static final int MAX_BYTES = 64;
    private final UserTokenService userTokenService;
    private final InlineKeyboardMarkupFactory inlineKeyboardMarkupFactory;
    private final InlineKeyboardButtonFactory inlineKeyboardButtonFactory;
    private final WebClient webClient;

    @Override
    public BotApiMethodMessage createBotApiMethodMessage(Update update) {
        UserToken userToken = findUserToken(update);
        String token = userToken.getToken();

        RepeatWord repeatWord = webClient.get()
                .uri("/api/repeat/learn-word")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(RepeatWord.class)
                .doOnSubscribe(sub -> System.out.println("Making request..."))
                .doOnSuccess(result -> System.out.println("Request successful: " + result))
                .doOnError(error -> System.out.println("Request failed: " + error.getMessage()))
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Request failed"));

        Random random = new Random();
        int randomIndex = random.nextInt();
        String word = randomIndex % 2 == 0
                ? repeatWord.getWord().getEn()
                : repeatWord.getWord().getRu();

        List<Word> responseOptions = repeatWord.getResponseOptions();
        String[] responseOptionsArray = new String[responseOptions.size()];

        for (int i = 0; i < responseOptions.size(); i++) {
            responseOptionsArray[i] = randomIndex % 2 == 0
                    ? extractCorrectWord(responseOptions.get(i).getRu())
                    : extractCorrectWord(responseOptions.get(i).getEn());
        }

        List<InlineKeyboardButton> inlineKeyboardButtons = inlineKeyboardButtonFactory
                .createInlineKeyboardButton(TEST_LEARNED_WORDS_CALL_BACK_QUERY, responseOptionsArray);

        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboardMarkupFactory
                .createInlineKeyboardMarkup(inlineKeyboardButtons);

        long chatId = UpdateService.extractChatId(update);

        return SendMessage.builder()
                .chatId(chatId)
                .text(word)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    private String extractCorrectWord(String word) {
        if (word == null || word.isEmpty()) return word;
        int parenIndex = word.indexOf('(');
        if (parenIndex != -1) {
            word = word.substring(0, parenIndex).trim();
        }

        if (word.getBytes(StandardCharsets.UTF_8).length >= MAX_BYTES) {
            int commaIndex = word.indexOf(',');
            if (commaIndex != -1) return word.substring(0, commaIndex);

            int spaceIndex = word.indexOf(' ');
            if (spaceIndex != -1) return word.substring(0, spaceIndex);
        }

        return word;
    }

    private UserToken findUserToken(Update update) {
        try {
            return userTokenService.findUserToken(update);
        } catch (UserTokenNotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}
