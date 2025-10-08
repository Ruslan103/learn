package com.english.enums;

import lombok.Getter;

@Getter
public enum Command {
    MAIN_MENU("Главное меню", "⬅️"),
    BACK("Назад", "⬅️"),
    DEFAULT("/rest"),
    LEARN_WORDS("Учить слова", "📚"),
    TEST_LEARNED_WORDS("Проверить выученные слова", "📖"),
    REGISTRATION("Зарегистрироваться", "✏️"),
    REGISTRATION_MENU("/register_menu"),
    SECRET_KEY("Отправит секретное слово", "📝"),
    START("Старт", "🚀"),
    PREFIX_START("/start"),
    LEARN_WORDS_BUTTONS_NAME("Учить слова", "\uD83D\uDCDA"),
    CONTINUE_LEARN_WORDS_BUTTONS_NAME("Далее", "\uD83D\uDCDA"),
    TEST_LEARN_WORDS_BUTTONS_NAME("Проверить выученные слова", "\uD83D\uDCD6"),
    TEST_LEARNED_WORDS_CALL_BACK_QUERY("LW");

    private final String text;
    private final String emoji;

    Command(String text, String emoji) {
        this.text = text;
        this.emoji = emoji;
    }

    Command(String text) {
        this.text = text;
        this.emoji = "";
    }

    public String value() {
        return text + emoji;
    }
}
