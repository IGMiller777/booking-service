package com.igmiller.booking.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class TimeSlot implements Comparable<TimeSlot> {
    private static final Duration MINIMUM_DURATION = Duration.ofMinutes(30);
    private static final Duration MAXIMUM_DURATION = Duration.ofHours(8);
    private static final int ALIGNMENT_MINUTES = 15;

    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final LocalDateTime start;
    private final LocalDateTime end;

    private TimeSlot(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end time must not be null");
        }

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (!isAlignedTo15(start) || !isAlignedTo15(end)) {
            throw new IllegalArgumentException("Start and end time must be aligned to 15");
        }

        Duration duration = Duration.between(start, end);

        if (duration.compareTo(MINIMUM_DURATION) < 0) {
            throw new IllegalArgumentException("Duration must be at least " + MINIMUM_DURATION);
        }

        if (duration.compareTo(MAXIMUM_DURATION) > 0) {
            throw new IllegalArgumentException("Duration must be at most " + MAXIMUM_DURATION);
        }

        this.start = start;
        this.end = end;
    }

    public static TimeSlot of(LocalDateTime startMinute, LocalDateTime endMinute) {
        return new TimeSlot(startMinute, endMinute);
    }

    public static TimeSlot of(LocalDate date, LocalTime from, LocalTime to) {
        return new TimeSlot(LocalDateTime.of(date, from), LocalDateTime.of(date, to));
    }

    public static TimeSlot ofHours(LocalDate date, int startHour, int endHour) {
        return of(date, LocalTime.of(startHour, 0), LocalTime.of(endHour, 0));
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public LocalDate getDate() {
        return start.toLocalDate();
    }

    public Duration duration() {
        return Duration.between(start, end);
    }


    public boolean overlaps(TimeSlot other) {
        return start.isBefore(other.end) && other.start.isBefore(end);
    }

    public boolean contain(LocalDateTime moment) {
        return !moment.isBefore(start) && moment.isBefore(end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimeSlot other = (TimeSlot) o;

        return start.equals(other.start) && end.equals(other.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return DISPLAY_FORMATTER.format(start) + " - " + DISPLAY_FORMATTER.format(end);
    }

    @Override
    public int compareTo(TimeSlot other) {
        int startComparison = start.compareTo(other.start);
        if (startComparison != 0) {
            return startComparison;
        }

        return end.compareTo(other.end);
    }

    private boolean isAlignedTo15(LocalDateTime dateTime) {
        return dateTime.getMinute() % ALIGNMENT_MINUTES == 0 && dateTime.getSecond() == 0 && dateTime.getNano() == 0;
    }

}
