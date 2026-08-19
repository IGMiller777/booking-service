package com.igmiller.booking.util;

public class TimeUtils {
    public static final int MINUTES_HAND = 60;
    public static final int ERROR_TIME = -1;
    public static final int MAX_HOUR = 23;
    public static final int MIN_MINUTE = 0;
    public static final int MAX_MINUTE = 60;
    public static final int MAX_DAY = 1440;

    public static int parseTime(String hhmm) {
        if (hhmm.length() != 5) {
            return ERROR_TIME;
        }

        int separateIndex = hhmm.indexOf(":");
        String hoursStr = hhmm.substring(0, separateIndex).trim();
        String minutesStr = hhmm.substring(separateIndex + 1).trim();

        int hours = Integer.parseInt(hoursStr);
        int minutes = Integer.parseInt(minutesStr);

        if (hours > MAX_HOUR || minutes > MAX_MINUTE) {
            return ERROR_TIME;
        }

        return hours * MINUTES_HAND + minutes;
    }

    public static String formatTime(int minutes) {
        if (!isValidTime(minutes)) {
            return "00:00";
        }

        int hours = minutes / MINUTES_HAND;
        int minute = minutes % MINUTES_HAND;

        return String.format("%02d:%02d", hours, minute);
    }

    public static boolean isValidTime(int minutes) {
        return minutes <= MAX_DAY;
    }

    public static boolean isAlignedTo15(int minutes) {
        return minutes % 15 == 0 || minutes % 15 == 10;
    }
}
