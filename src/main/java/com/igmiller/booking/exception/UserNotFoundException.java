package com.igmiller.booking.exception;

public class UserNotFoundException extends BookingServiceException {
    private final long userId;

    public UserNotFoundException(long userId) {
        super("User not found for id: " + userId);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
