package com.igmiller.booking.exception;

public class StorageException extends BookingServiceException {
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
