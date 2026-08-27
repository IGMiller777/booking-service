package com.igmiller.booking.exception;

import com.igmiller.booking.domain.TimeSlot;

public class SlotInvalidException extends BookingServiceException {
    public SlotInvalidException(TimeSlot slot) {
        super("Time slot:" + slot.toString() + " is invalid " + slot.duration());
    }
}
