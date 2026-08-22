package com.igmiller.booking.exception;

import com.igmiller.booking.domain.TimeSlot;

public class SlotUnavailableException extends BookingServiceException {
    private final long resourceId;
    private final TimeSlot conflictingSlot;

    public SlotUnavailableException(long resourceId, TimeSlot conflictingSlot) {
        super("Conflict in booking Slot: " + conflictingSlot + ". Resource booked:" + resourceId);
        this.resourceId = resourceId;
        this.conflictingSlot = conflictingSlot;
    }

    public TimeSlot getConflictingSlot() {
        return conflictingSlot;
    }

    public long getResourceId() {
        return resourceId;
    }
}
