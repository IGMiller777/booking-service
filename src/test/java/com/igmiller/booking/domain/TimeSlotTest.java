package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {
    // Фиксированная дата: TimeSlot не знает про "сейчас", и тесту незачем от него зависеть.
    private static final LocalDate DATE = LocalDate.of(2026, 11, 2);

    private static TimeSlot slot(int fromHour, int fromMinute, int toHour, int toMinute) {
        return TimeSlot.of(DATE, LocalTime.of(fromHour, fromMinute), LocalTime.of(toHour, toMinute));
    }

    // ---- overlaps (I3): a.start < b.end && b.start < a.end ----

    @Test
    void overlaps_shouldReturnFalse_whenSlotsOnlyTouchAtBoundary() {
        TimeSlot a = slot(10, 0, 11, 0);
        TimeSlot b = slot(11, 0, 12, 0);

        assertFalse(a.overlaps(b));
        assertFalse(b.overlaps(a));
    }

    @Test
    void overlaps_shouldReturnTrue_whenSlotsAreIdentical() {
        assertTrue(slot(10, 0, 11, 0).overlaps(slot(10, 0, 11, 0)));
    }

    @Test
    void overlaps_shouldReturnTrue_whenSlotsPartiallyOverlap_inBothDirections() {
        TimeSlot early = slot(10, 0, 11, 0);
        TimeSlot late = slot(10, 30, 11, 30);

        assertTrue(early.overlaps(late));
        assertTrue(late.overlaps(early));
    }

    @Test
    void overlaps_shouldReturnTrue_whenOneSlotContainsAnother_inBothDirections() {
        TimeSlot outer = slot(9, 0, 13, 0);
        TimeSlot inner = slot(10, 0, 11, 0);

        assertTrue(outer.overlaps(inner));
        assertTrue(inner.overlaps(outer));
    }

    @Test
    void overlaps_shouldReturnTrue_whenSlotsShareStartButDifferInLength() {
        assertTrue(slot(10, 0, 11, 0).overlaps(slot(10, 0, 12, 0)));
        assertTrue(slot(10, 0, 12, 0).overlaps(slot(10, 0, 11, 0)));
    }

    @Test
    void overlaps_shouldReturnFalse_whenSlotsAreDisjoint() {
        assertFalse(slot(8, 0, 9, 0).overlaps(slot(15, 0, 16, 0)));
        assertFalse(slot(15, 0, 16, 0).overlaps(slot(8, 0, 9, 0)));
    }

    // ---- конструктор: I1 и I2 ----

    @Test
    void of_shouldRejectSlot_whenStartEqualsEnd() {
        LocalDateTime t = LocalDateTime.of(DATE, LocalTime.of(10, 0));

        assertThrows(IllegalArgumentException.class, () -> TimeSlot.of(t, t));
    }

    @Test
    void of_shouldRejectSlot_whenEndIsBeforeStart() {
        assertThrows(IllegalArgumentException.class, () -> slot(11, 0, 10, 0));
    }

    @Test
    void of_shouldRejectSlot_whenBoundaryIsNotAlignedTo15Minutes() {
        assertThrows(IllegalArgumentException.class, () -> slot(10, 10, 11, 0));
        assertThrows(IllegalArgumentException.class, () -> slot(10, 0, 11, 5));
    }

    @Test
    void of_shouldRejectSlot_whenBoundaryHasSeconds() {
        LocalDateTime start = LocalDateTime.of(DATE, LocalTime.of(10, 0, 30));
        LocalDateTime end = LocalDateTime.of(DATE, LocalTime.of(11, 0));

        assertThrows(IllegalArgumentException.class, () -> TimeSlot.of(start, end));
    }

    @Test
    void of_shouldRejectSlot_whenShorterThan30Minutes() {
        assertThrows(IllegalArgumentException.class, () -> slot(10, 0, 10, 15));
    }

    @Test
    void of_shouldAcceptSlot_whenExactly30Minutes() {
        assertEquals(Duration.ofMinutes(30), slot(10, 0, 10, 30).duration());
    }

    @Test
    void of_shouldAcceptSlot_whenExactly8Hours() {
        assertEquals(Duration.ofHours(8), slot(9, 0, 17, 0).duration());
    }

    @Test
    void of_shouldRejectSlot_whenLongerThan8Hours() {
        assertThrows(IllegalArgumentException.class, () -> slot(9, 0, 17, 15));
    }

    // ---- contains: начало включительно, конец исключительно ----

    @Test
    void contains_shouldIncludeStart_andExcludeEnd() {
        TimeSlot s = slot(10, 0, 11, 0);

        assertTrue(s.contains(LocalDateTime.of(DATE, LocalTime.of(10, 0))));
        assertTrue(s.contains(LocalDateTime.of(DATE, LocalTime.of(10, 59))));
        assertFalse(s.contains(LocalDateTime.of(DATE, LocalTime.of(11, 0))));
        assertFalse(s.contains(LocalDateTime.of(DATE, LocalTime.of(9, 59))));
    }
}
