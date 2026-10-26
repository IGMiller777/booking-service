package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingTest {

    private Booking confirmed() {
        return Booking.of(1L, 1L, TimeSlot.ofHours(LocalDate.now().plusDays(1), 10, 11), Money.of("100", Currency.USD));
    }

    @Test
    void complete_shouldMoveConfirmedToCompleted() {
        Booking booking = confirmed();

        booking.complete();

        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
    }

    @Test
    void complete_shouldRejectCancelledBooking() {
        Booking booking = confirmed();
        booking.cancel();

        assertThrows(IllegalStateException.class, booking::complete);
    }
}
