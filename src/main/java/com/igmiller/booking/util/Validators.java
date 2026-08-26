package com.igmiller.booking.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validators {
    private static final Pattern RESOURCE_CODE = Pattern.compile("^[A-Z]{2,6}-\\d{2,4}$");
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern TIME_RANGE = Pattern.compile("^(?<startHour>[01]\\d|2[0-3]):(?<startMinute>[0-5]\\d)-(?<endHour>[01]\\d|2[0-3]):(?<endMinute>[0-5]\\d)$");

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

    public static int[] isValidTimeRange(String time) {
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }

        Matcher matcher = TIME_RANGE.matcher(time);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }

        int startHour = Integer.parseInt(matcher.group("startHour"));
        int startMinute = Integer.parseInt(matcher.group("startMinute"));
        int endHour = Integer.parseInt(matcher.group("endHour"));
        int endMinute = Integer.parseInt(matcher.group("endMinute"));
        int start = startHour * 60 + startMinute;
        int end = endHour * 60 + endMinute;

        return new int[]{start, end};
    }
}
