package com.iyed_houhou.inventoryManagementApp.utils;

public class ValidationUtils {
    private static final String EMAIL_REGEX = "^[\\w-_.+]*[\\w-_.]@(\\w+\\.)+\\w+\\w$";

    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }
}
