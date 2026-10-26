package com.igmiller.booking.exception;

public class CancellationNotAllowedException extends BookingServiceException {
    private final long bookingId;
    private final String reason;

    public CancellationNotAllowedException(long bookingId, String reason) {
        super("Cannot cancel booking " + bookingId + ": " + reason);
        this.bookingId = bookingId;
        this.reason = reason;
    }

    public long getBookingId() {
        return bookingId;
    }

    public String getReason() {
        return reason;
    }
}
