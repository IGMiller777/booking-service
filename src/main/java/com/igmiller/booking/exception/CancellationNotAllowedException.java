package com.igmiller.booking.exception;

public class CancellationNotAllowedException extends BookingServiceException {
    public final long bookingId;

    public CancellationNotAllowedException(long bookingId) {
        super("Cannot cancel booking");
        this.bookingId = bookingId;
    }

    public long getBookingId() {
        return bookingId;
    }
}
