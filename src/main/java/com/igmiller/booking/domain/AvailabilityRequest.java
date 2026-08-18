package com.igmiller.booking.domain;

import com.igmiller.booking.util.TimeUtils;

public record AvailabilityRequest(long resourceId, int date, TimeSlot slot) {
    public AvailabilityRequest {
        if(date < 0) {
            throw new IllegalArgumentException("Date cannot be negative");
        }
    }
}
