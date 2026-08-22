package com.igmiller.booking.exception;

public class BookingLimitExceededException extends BookingServiceException {
    private final long userId;
    private final int limit;

    public BookingLimitExceededException(long userId, int limit) {
        super("User: " + userId + " is limited to " + limit);

        this.userId = userId;
        this.limit = limit;
    }

    public long getUserId() {
        return userId;
    }

    public int getLimit() {
        return limit;
    }
}
