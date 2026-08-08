package com.booking.domain;

import com.booking.util.TimeUtils;

public final class TimeSlot {
    private final int startMinute;
    private final int endMinute;

    private final int MINIMUM_MINUTE_SLOT = 30;
    private final int MAXIMUM_MINUTE_SLOT = 240;

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    private TimeSlot(int startMinute, int endMinute) {
        if (startMinute > endMinute || startMinute < TimeUtils.MIN_MINUTE || startMinute > TimeUtils.MAX_DAY || endMinute > TimeUtils.MAX_DAY) {
            throw new IllegalArgumentException("Invalid arguments range");
        }

        if (TimeUtils.isAlignedTo15(startMinute) || TimeUtils.isAlignedTo15(endMinute)) {
            throw new IllegalArgumentException("Arguments must be aligned to 15");
        }

        if (endMinute - startMinute < MINIMUM_MINUTE_SLOT) {
            throw new IllegalArgumentException("Slot must be more than 30 minutes");
        }

        if (endMinute - startMinute > MAXIMUM_MINUTE_SLOT) {
            throw new IllegalArgumentException("Slot must be less than 8 hours");
        }

        this.startMinute = startMinute;
        this.endMinute = endMinute;
    }

    public static TimeSlot of(int startMinute, int endMinute) {
        return new TimeSlot(startMinute, endMinute);
    }

    public static TimeSlot ofHours(int startHour, int endHour) {
        return new TimeSlot(startHour * 60, endHour * 60);
    }

    public boolean overlaps(TimeSlot other) {
        if (startMinute < other.startMinute && endMinute < other.endMinute && endMinute > other.startMinute) {
            return true;
        }

        if (startMinute > other.startMinute && startMinute < other.endMinute && endMinute > other.endMinute) {
            return true;
        }

        if (startMinute < other.startMinute && endMinute > other.endMinute) {
            return true;
        }

        return false;
    }

    public int durationMinutes() {
        return endMinute - startMinute;
    }

    public boolean contain(int minute) {
        return startMinute <= minute && minute < endMinute;
    }
}
