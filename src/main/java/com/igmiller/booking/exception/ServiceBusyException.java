package com.igmiller.booking.exception;

public class ServiceBusyException extends BookingServiceException {
    public ServiceBusyException(String message) {
        super(message);
    }
}
