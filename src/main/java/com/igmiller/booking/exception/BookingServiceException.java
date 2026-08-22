package com.igmiller.booking.exception;

public abstract class BookingServiceException extends RuntimeException {
    protected BookingServiceException(String message) {
        super(message);
    }

    protected BookingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

