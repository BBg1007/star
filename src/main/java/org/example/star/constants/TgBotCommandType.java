package org.example.star.constants;

import java.util.Arrays;

public enum TgBotCommandType {
    RECOMMEND("/recommend"),
    NOT_FOUND("/IDK");
    private final String type;

    TgBotCommandType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static TgBotCommandType checkCommandType(String text) {
        return Arrays.stream(getValues())
                .filter(type -> text.toLowerCase().contains(type.getType()))
                .findFirst()
                .orElse(TgBotCommandType.NOT_FOUND);

    }

    private static TgBotCommandType[] getValues() {
        return TgBotCommandType.values();
    }
}
