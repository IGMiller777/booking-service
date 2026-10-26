package com.igmiller.booking.exception;

import com.igmiller.booking.domain.TimeSlot;

public class SlotInPastException extends BookingServiceException {
    private final TimeSlot slot;

    public SlotInPastException(TimeSlot slot) {
        super("Time slot " + slot + " is not in the future");
        this.slot = slot;
    }

    public TimeSlot getSlot() {
        return slot;
    }
}
