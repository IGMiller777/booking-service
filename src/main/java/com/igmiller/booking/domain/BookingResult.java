package com.igmiller.booking.domain;

public sealed interface BookingResult permits BookingResult.Success, BookingResult.Conflict {
    record Success(Booking booking) implements BookingResult {
    }

    record Conflict(Booking existing) implements BookingResult {
    }
}
