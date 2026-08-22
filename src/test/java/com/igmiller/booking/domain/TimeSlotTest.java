package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    void overlaps_shouldReturnFalse_whenSlotsOnlyTouchAtBoundary() {
        TimeSlot a = TimeSlot.ofHours(10, 11);
        TimeSlot b = TimeSlot.ofHours(11, 12);

        boolean result = a.overlaps(b);

        assertFalse(result);
    }

    @Test
    void of_shouldThrowIllegalArgumentException_whenStartIsAfterEnd() {
        assertThrows(IllegalArgumentException.class, () -> TimeSlot.of(600, 540));
    }
}