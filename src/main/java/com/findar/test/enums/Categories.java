package com.findar.test.enums;

import java.util.HashMap;
import java.util.Map;

public enum Categories {
    LITERATURE(0),
    FICTION(1),
    ACTION(2),
    THRILLER(3),
    TECHNOLOGY(4),
    DRAMA(5),
    POETRY(6),
    OTHERS(7);

    private int value;
    private static Map map = new HashMap<>();

    private Categories(int value) {
        this.value = value;
    }

    static {
        for (Categories categories : Categories.values()) {
            map.put(categories.value, categories);
        }
    }

    public static Categories valueOf(int category) {
        return (Categories) map.get(category);
    }

    public int getValue() {
        return value;
    }
}