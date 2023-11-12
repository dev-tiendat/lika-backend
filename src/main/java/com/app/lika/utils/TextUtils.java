package com.app.lika.utils;

public class TextUtils {
    public static String[] LIST_CHAR = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static String cleanseString(String input) {
        if (input != null) {
            input = input.trim();

            return input;
        }

        return null;
    }
}

