package com.igmiller.booking.domain;

import com.igmiller.booking.util.TimeUtils;

import java.util.Objects;

public final class TimeSlot {
    private final int startMinute;
    private final int endMinute;

    private static final int MINIMUM_MINUTE_SLOT = 30;
    private static final int MAXIMUM_MINUTE_SLOT = 240;

    private TimeSlot(int startMinute, int endMinute) {
        if (startMinute > endMinute || startMinute < TimeUtils.MIN_MINUTE || startMinute > TimeUtils.MAX_DAY || endMinute > TimeUtils.MAX_DAY) {
            throw new IllegalArgumentException("Invalid arguments range");
        }

        if (!TimeUtils.isAlignedTo15(startMinute) || !TimeUtils.isAlignedTo15(endMinute)) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimeSlot timeSlot = (TimeSlot) o;

        return startMinute == timeSlot.startMinute && endMinute == timeSlot.endMinute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startMinute, endMinute);
    }

    @Override
    public String toString() {
        return TimeUtils.formatTime(duration());
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public int duration() {
        return endMinute - startMinute;
    }

    public static TimeSlot of(int startMinute, int endMinute) {
        return new TimeSlot(startMinute, endMinute);
    }

    public static TimeSlot ofHours(int startHour, int endHour) {
        return new TimeSlot(startHour * 60, endHour * 60);
    }

    public boolean overlaps(TimeSlot other) {
        return startMinute < other.endMinute && other.startMinute < endMinute;
    }

    public int durationMinutes() {
        return endMinute - startMinute;
    }

    public boolean contain(int minute) {
        return startMinute <= minute && minute < endMinute;
    }
}
