package com.igmiller.booking.exception;

public class BookingNotFoundException extends BookingServiceException {
    private final long bookingId;

    public BookingNotFoundException(long bookingId) {
        super("Booking not found for id: " + bookingId);
        this.bookingId = bookingId;
    }

    public long getBookingId() {
        return bookingId;
    }
}
