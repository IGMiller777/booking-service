package com.igmiller.booking.domain;

public enum ResourceType {
    MEETING_ROOM(30),
    DESK(30),
    EQUIPMENT(60),
    COURT(60);

    private final int minBookingMinutes;

    ResourceType(int minBookingMinutes) {
        this.minBookingMinutes = minBookingMinutes;
    }

    public int getMinBookingMinutes() {
        return minBookingMinutes;
    }

    public boolean canBeBookedFor(TimeSlot slot) {
        return slot.duration().toMinutes() >= minBookingMinutes;
    }
}
