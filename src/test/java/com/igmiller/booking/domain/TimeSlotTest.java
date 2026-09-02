package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    void overlaps_shouldReturnFalse_whenSlotsOnlyTouchAtBoundary() {
        TimeSlot a = TimeSlot.ofHours(LocalDate.now(), 10, 11);
        TimeSlot b = TimeSlot.ofHours(LocalDate.now(), 11, 12);

        boolean result = a.overlaps(b);

        assertFalse(result);
    }
}