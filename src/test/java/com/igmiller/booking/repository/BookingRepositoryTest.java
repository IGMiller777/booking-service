package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.domain.Money;
import com.igmiller.booking.domain.Currency;
import com.igmiller.booking.domain.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BookingRepositoryTest {

    private BookingsRepository repository;

    @BeforeEach
    void setUp() {
        repository = new BookingsRepository();
    }

    private Booking newBooking() {
        return Booking.of(1L, 1L, TimeSlot.ofHours(LocalDate.now(), 10, 12), Money.of("500", Currency.EUR));
    }

    @Test
    void shouldCreate() {
        Booking booking = newBooking();

        repository.save(booking);

        assertEquals(booking, repository.findById(booking.getId()));
    }

    @Test
    void should_returnNull_ifNoExists() {
        Booking booking = repository.findById(1L);

        assertNull(booking);
    }

    @Test
    void should_replaceExisting_ifExists() {
        Booking booking = newBooking();

        repository.save(booking);
        booking.cancel();
        repository.save(booking);

        assertEquals(1, repository.findAll().size());
        assertEquals(1, repository.findByUserId(1L).size());
        assertEquals(1, repository.findByResourceId(1L).size());
    }

    @Test
    void delete_shouldRemoveBookingFromAllIndexes() {
        Booking booking = newBooking();
        repository.save(booking);

        repository.delete(booking.getId());

        assertNull(repository.findById(booking.getId()));
        assertTrue(repository.findByUserId(1L).isEmpty());
        assertTrue(repository.findByResourceId(1L).isEmpty());
    }

    @Test
    void findByUserId_shouldReturnEmptyList_whenUserHasNoBookings() {
        assertTrue(repository.findByUserId(999L).isEmpty());
    }
}
