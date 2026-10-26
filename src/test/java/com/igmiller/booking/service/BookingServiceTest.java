package com.igmiller.booking.service;

import com.igmiller.booking.domain.*;
import com.igmiller.booking.exception.*;
import com.igmiller.booking.repository.BookingsRepository;
import com.igmiller.booking.repository.ResourcesRepository;
import com.igmiller.booking.repository.UsersRepository;
import com.igmiller.booking.util.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 26);
    private static final LocalDate TOMORROW = TODAY.plusDays(1);

    private BookingsRepository bookings;
    private BookingService service;
    private User ivan;
    private User petr;
    private User admin;
    private Resource room;

    @BeforeEach
    void setUp() {
        // "сейчас" зафиксировано: 2026-10-26 12:00
        Clock clock = Clock.fixed(LocalDateTime.of(TODAY, java.time.LocalTime.NOON).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

        bookings = new BookingsRepository();
        ResourcesRepository resources = new ResourcesRepository();
        UsersRepository users = new UsersRepository();

        ivan = users.save(User.of("Ivan", "ivan@example.com", Role.USER));
        petr = users.save(User.of("Petr", "petr@example.com", Role.USER));
        admin = users.save(User.of("Admin", "admin@example.com", Role.ADMIN));
        room = resources.save(Resource.of("Room A", "MR-101", 8, Money.of("100", Currency.RUB),
                ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE));

        AppConfig config = AppConfig.load(Path.of("no-such-config.properties"));
        service = new BookingService(bookings, resources, users, new PricingService(), config, clock);
    }

    private TimeSlot slot(LocalDate date, int from, int to) {
        return TimeSlot.ofHours(date, from, to);
    }

    private Booking book(User user, TimeSlot slot) {
        return service.book(user.getId(), room.getId(), slot);
    }

    // ---- первая бронь пользователя (регрессия на NPE) ----

    @Test
    void book_shouldSucceed_forUserWithoutAnyBookings() {
        Booking booking = book(ivan, slot(TOMORROW, 10, 12));

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(Currency.RUB, booking.getPrice().getCurrency());
        assertEquals(1, service.findUserBooking(ivan.getId()).size());
    }

    // ---- I4 ----

    @Test
    void book_shouldRejectSlotInThePast() {
        assertThrows(SlotInPastException.class, () -> book(ivan, slot(TODAY, 8, 9)));
    }

    @Test
    void book_shouldRejectSlotStartingExactlyNow() {
        assertThrows(SlotInPastException.class, () -> book(ivan, slot(TODAY, 12, 13)));
    }

    @Test
    void book_shouldAcceptSlotStartingAfterNow() {
        assertDoesNotThrow(() -> book(ivan, slot(TODAY, 13, 14)));
    }

    // ---- пользователь ----

    @Test
    void book_shouldRejectUnknownUser() {
        assertThrows(UserNotFoundException.class,
                () -> service.book(9_999L, room.getId(), slot(TOMORROW, 10, 12)));
    }

    // ---- I3 ----

    @Test
    void book_shouldThrowSlotUnavailable_withConflictingSlot_whenOverlap() {
        TimeSlot taken = slot(TOMORROW, 10, 12);
        book(ivan, taken);

        SlotUnavailableException e = assertThrows(SlotUnavailableException.class,
                () -> book(petr, slot(TOMORROW, 11, 13)));

        assertEquals(taken, e.getConflictingSlot());
        assertEquals(room.getId(), e.getResourceId());
    }

    @Test
    void book_shouldAllowSlotsTouchingAtBoundary() {
        book(ivan, slot(TOMORROW, 10, 12));

        assertDoesNotThrow(() -> book(petr, slot(TOMORROW, 12, 14)));
    }

    @Test
    void book_shouldAllowSameSlot_afterCancellation() {
        Booking first = book(ivan, slot(TOMORROW, 10, 12));
        service.cancel(ivan.getId(), first.getId());

        assertDoesNotThrow(() -> book(petr, slot(TOMORROW, 10, 12)));
    }

    // ---- I8 ----

    @Test
    void book_shouldRejectSixthUpcomingBooking() {
        for (int hour = 8; hour < 13; hour++) {
            book(ivan, slot(TOMORROW, hour, hour + 1));
        }

        assertThrows(BookingLimitExceededException.class, () -> book(ivan, slot(TOMORROW, 15, 16)));
    }

    @Test
    void book_shouldNotCountPastBookingsTowardsLimit() {
        for (int i = 0; i < 5; i++) {
            bookings.save(Booking.restore(7_000L + i, ivan.getId(), room.getId(),
                    slot(TODAY.minusDays(5 + i), 10, 11), Money.of("100", Currency.RUB), BookingStatus.CONFIRMED));
        }

        assertDoesNotThrow(() -> book(ivan, slot(TOMORROW, 10, 12)));
    }

    @Test
    void book_shouldNotCountCancelledBookingsTowardsLimit() {
        for (int hour = 8; hour < 13; hour++) {
            Booking booking = book(ivan, slot(TOMORROW, hour, hour + 1));
            service.cancel(ivan.getId(), booking.getId());
        }

        assertDoesNotThrow(() -> book(ivan, slot(TOMORROW, 15, 16)));
    }

    // ---- I7 ----

    @Test
    void cancel_shouldWork_whenMoreThanTwoHoursBeforeStart() {
        Booking booking = book(ivan, slot(TOMORROW, 10, 12));

        Booking cancelled = service.cancel(ivan.getId(), booking.getId());

        assertEquals(BookingStatus.CANCELLED, cancelled.getStatus());
    }

    @Test
    void cancel_shouldWork_exactlyTwoHoursBeforeStart() {
        Booking booking = book(ivan, slot(TODAY, 14, 15));

        assertDoesNotThrow(() -> service.cancel(ivan.getId(), booking.getId()));
    }

    @Test
    void cancel_shouldBeRejected_lessThanTwoHoursBeforeStart() {
        Booking booking = book(ivan, slot(TODAY, 13, 14));

        assertThrows(CancellationNotAllowedException.class, () -> service.cancel(ivan.getId(), booking.getId()));
    }

    @Test
    void cancel_shouldBeRejected_forForeignBooking() {
        Booking booking = book(ivan, slot(TOMORROW, 10, 12));

        assertThrows(CancellationNotAllowedException.class, () -> service.cancel(petr.getId(), booking.getId()));
    }

    @Test
    void cancel_shouldBeAllowedForAdmin_onForeignBooking() {
        Booking booking = book(ivan, slot(TOMORROW, 10, 12));

        Booking cancelled = service.cancel(admin.getId(), booking.getId());

        assertEquals(BookingStatus.CANCELLED, cancelled.getStatus());
    }

    @Test
    void cancel_shouldBeRejected_forAlreadyCancelledBooking() {
        Booking booking = book(ivan, slot(TOMORROW, 10, 12));
        service.cancel(ivan.getId(), booking.getId());

        assertThrows(CancellationNotAllowedException.class, () -> service.cancel(ivan.getId(), booking.getId()));
    }

    @Test
    void cancel_shouldRejectUnknownBooking() {
        assertThrows(BookingNotFoundException.class, () -> service.cancel(ivan.getId(), 424_242L));
    }

    @Test
    void cancel_shouldBeRejected_forNonConfirmedBooking_evenIfStartIsFarAway() {
        // статус проверяется отдельно от времени: COMPLETED с будущим слотом (искусственно) всё равно не отменяется
        Booking completed = bookings.save(Booking.restore(8_004L, ivan.getId(), room.getId(),
                slot(TOMORROW.plusDays(3), 10, 11), Money.of("100", Currency.RUB), BookingStatus.COMPLETED));

        assertThrows(CancellationNotAllowedException.class, () -> service.cancel(ivan.getId(), completed.getId()));
    }

    // ---- COMPLETED ----

    @Test
    void completeFinishedBookings_shouldMoveOnlyFinishedConfirmedBookings() {
        Booking finished = bookings.save(Booking.restore(8_001L, ivan.getId(), room.getId(),
                slot(TODAY, 8, 10), Money.of("100", Currency.RUB), BookingStatus.CONFIRMED));
        Booking upcoming = book(ivan, slot(TOMORROW, 10, 12));
        Booking cancelled = bookings.save(Booking.restore(8_002L, ivan.getId(), room.getId(),
                slot(TODAY, 6, 7), Money.of("100", Currency.RUB), BookingStatus.CANCELLED));

        int completed = service.completeFinishedBookings();

        assertEquals(1, completed);
        assertEquals(BookingStatus.COMPLETED, finished.getStatus());
        assertEquals(BookingStatus.CONFIRMED, upcoming.getStatus());
        assertEquals(BookingStatus.CANCELLED, cancelled.getStatus());
        assertEquals(0, service.completeFinishedBookings(), "second run must be a no-op");
    }

    @Test
    void cancel_shouldBeRejected_forCompletedBooking() {
        Booking finished = bookings.save(Booking.restore(8_003L, ivan.getId(), room.getId(),
                slot(TODAY, 8, 10), Money.of("100", Currency.RUB), BookingStatus.CONFIRMED));
        service.completeFinishedBookings();

        assertThrows(CancellationNotAllowedException.class, () -> service.cancel(ivan.getId(), finished.getId()));
    }
}
