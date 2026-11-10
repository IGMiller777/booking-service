package com.igmiller.booking.repository;

import com.igmiller.booking.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryBookingRepositoryTest {

    private InMemoryBookingRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBookingRepository();
    }

    private Booking newBooking() {
        return Booking.of(1L, 1L, TimeSlot.ofHours(LocalDate.now(), 10, 12), Money.of("500", Currency.EUR));
    }

    @Test
    void save_shouldMakeBookingFindableById() {
        Booking saved = repository.save(newBooking());

        assertEquals(saved, repository.findById(saved.getId()));
    }

    @Test
    void findById_shouldReturnNull_whenBookingDoesNotExist() {
        Booking booking = repository.findById(1L);

        assertNull(booking);
    }

    @Test
    void save_shouldReplaceExisting_whenSameBookingSavedTwice() {
        Booking saved = repository.save(newBooking());

        saved.cancel();
        repository.save(saved);

        assertEquals(1, repository.findAll().size());
        assertEquals(1, repository.findByUserId(1L).size());
        assertEquals(1, repository.findByResourceId(1L).size());
    }

    @Test
    void delete_shouldRemoveBookingFromAllIndexes() {
        Booking saved = repository.save(newBooking());

        repository.delete(saved.getId());

        assertNull(repository.findById(saved.getId()));
        assertTrue(repository.findByUserId(1L).isEmpty());
        assertTrue(repository.findByResourceId(1L).isEmpty());
    }

    @Test
    void findByUserId_shouldReturnEmptyList_whenUserHasNoBookings() {
        assertTrue(repository.findByUserId(999L).isEmpty());
    }

    @Test
    void save_shouldAssignId_whenBookingIsNew() {
        Booking booking = newBooking();

        Booking saved = repository.save(booking);

        assertNull(booking.getId());
        assertEquals(1L, saved.getId());
        assertEquals(booking.getSlot(), saved.getSlot());
    }

    @Test
    void save_shouldContinueNumbering_afterRestoredId() {
        repository.save(Booking.restore(10L, 1L, 1L, TimeSlot.ofHours(LocalDate.now(), 8, 9),
                Money.of("250", Currency.EUR), BookingStatus.CONFIRMED));

        Booking saved = repository.save(newBooking());

        assertEquals(11L, saved.getId());
    }
}
