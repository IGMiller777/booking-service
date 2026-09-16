package com.igmiller.booking.domain;

public enum ResourceType {
    MEETING_ROOM("Speaking Room", 30, true),
    DESK("Working Room", 15, false),
    EQUIPMENT("Equipment", 60, false),
    COURT("Court", 60, true);

    private final String displayName;
    private final int minBookingMinutes;
    private final boolean requireCapacity;

    ResourceType(String displayName, int minBookingMinutes, boolean requireCapacity) {
        this.displayName = displayName;
        this.minBookingMinutes = minBookingMinutes;
        this.requireCapacity = requireCapacity;
    }

    public int getMinBookingMinutes() {
        return minBookingMinutes;
    }

    public boolean canBeBookedFor(TimeSlot slot) {
        return slot.duration().toMinutes() >= minBookingMinutes;
    }
}
