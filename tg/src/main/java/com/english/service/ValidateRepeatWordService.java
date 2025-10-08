package com.english.service;

import com.english.annotations.TelegramCommand;
import com.english.exceptions.UserNotFoundException;
import com.english.exceptions.UserTokenNotFoundException;
import com.english.model.UserToken;
import com.english.model.ValidateWord;
import com.english.model.Word;
import com.english.service.interfaces.BotApiMethodMessageService;
import com.english.service.interfaces.UserTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.english.enums.Command.TEST_LEARNED_WORDS_CALL_BACK_QUERY;

@Service
@TelegramCommand(TEST_LEARNED_WORDS_CALL_BACK_QUERY)
public class ValidateRepeatWordService implements BotApiMethodMessageService {
    private final WebClient webClient;
    private final BotApiMethodMessageService botApiMethodMessageService;
    private final UserTokenService userTokenService;

    public ValidateRepeatWordService(@Qualifier("repeatWordService")
                                     BotApiMethodMessageService botApiMethodMessageService,
                                     WebClient webClient,
                                     UserTokenService userTokenService) {
        this.botApiMethodMessageService = botApiMethodMessageService;
        this.webClient = webClient;
        this.userTokenService = userTokenService;
    }

    @Override
    public BotApiMethodMessage createBotApiMethodMessage(Update update) {
        String validateText = update.hasCallbackQuery()
                ? update.getCallbackQuery().getData().split("/")[1]
                : update.getMessage().getText();

        UserToken userToken = findUserToken(update);
        String token = userToken.getToken();

        ValidateWord validateWord = webClient.post()
                .uri("/api/repeat/check-learn-word")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validateText)
                .retrieve()
                .bodyToMono(ValidateWord.class)
                .block();

        String trueWord = Optional.ofNullable(validateWord)
                .map(ValidateWord::getTrueWord)
                .map(Word::toString)
                .orElseThrow();

        String text = validateWord.isTrue()
                ? "Верно! "
                : "Не-а, правильное слово - \n" + trueWord + "! \n";

        SendMessage nextSendMessage = (SendMessage) botApiMethodMessageService
                .createBotApiMethodMessage(update);

        String textFromNextSendMessage = nextSendMessage.getText();
        nextSendMessage.setText(text + " \nСледующее слово - " + textFromNextSendMessage);
        return nextSendMessage;
    }

    private UserToken findUserToken(Update update) {
        try {
            return userTokenService.findUserToken(update);
        } catch (UserTokenNotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}
