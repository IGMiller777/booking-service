package com.igmiller.booking.service;

import com.igmiller.booking.domain.*;
import com.igmiller.booking.exception.*;
import com.igmiller.booking.repository.BookingRepository;
import com.igmiller.booking.repository.ResourceRepository;
import com.igmiller.booking.repository.UserRepository;
import com.igmiller.booking.util.AppConfig;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

public class BookingService {
    private final ConcurrentHashMap<Long, ReentrantLock> resourceLocks = new ConcurrentHashMap<>();
    private final int maxActiveBookingsPerUser;
    private final Duration cancelWindow;

    private final BookingRepository bookingRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;
    private final PricingService pricingService;
    private final Clock clock;

    private final AtomicLong totalBookings = new AtomicLong();
    private final AtomicLong rejectedBookings = new AtomicLong();
    private final LongAdder concurrentRequests = new LongAdder();

    public BookingService(BookingRepository bookingRepository, ResourceRepository resourceRepository, UserRepository userRepository, PricingService pricingService, AppConfig appConfig, Clock clock) {
        this.bookingRepository = bookingRepository;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
        this.pricingService = pricingService;
        this.clock = clock;
        this.maxActiveBookingsPerUser = appConfig.getInt("bookings.max.per.user", 5);
        this.cancelWindow = Duration.ofHours(appConfig.getInt("bookings.cancel.window.hours", 2));
    }

    public Booking book(long userId, long resourceId, TimeSlot slot) {
        concurrentRequests.increment();

        try {
            ReentrantLock lock = acquireLock(resourceId);

            try {
                LocalDateTime now = LocalDateTime.now(clock);

                // Only future
                if (!slot.getStart().isAfter(now)) {
                    throw new SlotInPastException(slot);
                }

                if (userRepository.findById(userId) == null) {
                    throw new UserNotFoundException(userId);
                }

                Resource resource = resourceRepository.findById(resourceId);

                if (resource == null) {
                    throw new ResourceNotFoundException(resourceId);
                }

                // Only active
                if (resource.getStatus() != ResourceStatus.ACTIVE) {
                    throw new ResourceUnavailableException(resourceId, resource.getStatus());
                }

                if (!resource.getResourceType().canBeBookedFor(slot)) {
                    throw new SlotInvalidException(slot);
                }

                // No more than n in the future
                long activeCount = countUpcomingConfirmed(findUserBooking(userId), now);

                if (activeCount >= maxActiveBookingsPerUser) {
                    throw new BookingLimitExceededException(userId, maxActiveBookingsPerUser);
                }

                // No conflicts
                Booking conflict = findConflicts(resourceId, slot);
                if (conflict != null) {
                    throw new SlotUnavailableException(resourceId, conflict.getSlot());
                }

                Money price = pricingService.calculatePrice(resource, slot);
                Booking booking = Booking.of(userId, resourceId, slot, price);
                bookingRepository.save(booking);
                totalBookings.incrementAndGet();

                return booking;
            } catch (BookingServiceException e) {
                rejectedBookings.incrementAndGet();
                throw e;
            } finally {
                lock.unlock();
            }

        } finally {
            concurrentRequests.decrement();
        }
    }

    public Booking cancel(long userId, long bookingId) {
        User actor = userRepository.findById(userId);
        if (actor == null) {
            throw new UserNotFoundException(userId);
        }

        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException(bookingId);
        }

        ReentrantLock lock = acquireLock(booking.getResourceId());

        try {
            Booking current = bookingRepository.findById(bookingId);

            if (current == null) {
                throw new BookingNotFoundException(bookingId);
            }

            // Foreign booking able to cancel only ADMIN
            if (current.getUserId() != userId && actor.getRole() != Role.ADMIN) {
                throw new CancellationNotAllowedException(bookingId, "booking belongs to another user");
            }

            if (current.getStatus() != BookingStatus.CONFIRMED) {
                throw new CancellationNotAllowedException(bookingId, "booking is already " + current.getStatus());
            }

            // No later that cancel window
            LocalDateTime deadline = current.getSlot().getStart().minus(cancelWindow);
            if (LocalDateTime.now(clock).isAfter(deadline)) {
                throw new CancellationNotAllowedException(bookingId,
                        "cancellation is allowed no later than " + cancelWindow.toHours() + " hours before the start");
            }

            current.cancel();
            bookingRepository.save(current);

            return current;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Move in COMPLETED all CONFIRMED-bookings, which slot closed.
     *
     * @return how much bookings moved in COMPLETED
     */
    public int completeFinishedBookings() {
        LocalDateTime now = LocalDateTime.now(clock);
        int completed = 0;

        for (Booking booking : bookingRepository.findAll()) {
            if (!isFinishedConfirmed(booking, now)) {
                continue;
            }

            ReentrantLock lock = acquireLock(booking.getResourceId());
            try {
                Booking current = bookingRepository.findById(booking.getId());

                if (current != null && isFinishedConfirmed(current, now)) {
                    current.complete();
                    bookingRepository.save(current);
                    completed++;
                }
            } finally {
                lock.unlock();
            }
        }

        return completed;
    }

    public List<Booking> findUserBooking(long userId) {
        return bookingRepository.findByUserId(userId);
    }

    private boolean isFinishedConfirmed(Booking booking, LocalDateTime now) {
        return booking.getStatus() == BookingStatus.CONFIRMED && !booking.getSlot().getEnd().isAfter(now);
    }

    private long countUpcomingConfirmed(List<Booking> bookings, LocalDateTime now) {
        long count = 0;

        for (Booking booking : bookings) {
            if (booking.getStatus() == BookingStatus.CONFIRMED && booking.getSlot().getStart().isAfter(now)) {
                count++;
            }
        }

        return count;
    }

    private Booking findConflicts(long resourceId, TimeSlot slot) {
        List<Booking> all = bookingRepository.findByResourceId(resourceId);
        for (Booking booking : all) {
            if (booking.getStatus() == BookingStatus.CONFIRMED && booking.getSlot().overlaps(slot)) {
                return booking;
            }
        }

        return null;
    }

    private ReentrantLock acquireLock(long resourceId) {
        ReentrantLock lock = lockFor(resourceId);
        boolean acquired;

        try {
            acquired = lock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceBusyException("Waiting for lock interrupted");
        }

        if (!acquired) {
            throw new ServiceBusyException("Resource is busy by other operation");
        }

        return lock;
    }

    private ReentrantLock lockFor(long resourceId) {
        return resourceLocks.computeIfAbsent(resourceId, id -> new ReentrantLock());
    }

    public long getTotalBookings() {
        return totalBookings.get();
    }

    public long getRejectedBookings() {
        return rejectedBookings.get();
    }

    public long getConcurrentRequests() {
        return concurrentRequests.sum();
    }
}
