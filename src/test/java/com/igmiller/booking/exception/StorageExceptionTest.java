package com.igmiller.booking.exception;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

class StorageExceptionTest {

    @Test
    void constructor_shouldExtendBookingServiceException_andKeepCause() {
        IOException cause = new IOException("disk is full");

        StorageException exception = new StorageException("Cant write data.csv", cause);

        assertInstanceOf(BookingServiceException.class, exception);
        assertSame(cause, exception.getCause());
    }
}
