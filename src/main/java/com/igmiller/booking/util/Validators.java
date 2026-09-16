package com.igmiller.booking.util;

import java.util.regex.Pattern;

public final class Validators {
    private static final Pattern RESOURCE_CODE = Pattern.compile("^[A-Z]{2,6}-\\d{2,4}$");
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public static boolean isValidResourceCode(String resourceCode) {
        if (resourceCode == null) {
            return false;
        }

        return RESOURCE_CODE.matcher(resourceCode).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        return EMAIL.matcher(email).matches();
    }
}
