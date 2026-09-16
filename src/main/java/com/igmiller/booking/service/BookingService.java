package com.igmiller.booking.service;

import com.igmiller.booking.domain.*;
import com.igmiller.booking.exception.*;
import com.igmiller.booking.repository.BookingRepository;
import com.igmiller.booking.repository.ResourceRepository;
import com.igmiller.booking.util.AppConfig;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

public class BookingService {
    private final ConcurrentHashMap<Long, ReentrantLock> resourceLocks = new ConcurrentHashMap<>();
    private int maxActiveBookingsPerUser = 5;

    private final BookingRepository bookingRepository;
    private final ResourceRepository resourceRepository;
    private final PricingService pricingService;

    private final AtomicLong totalBookings = new AtomicLong();
    private final AtomicLong rejectedBookings = new AtomicLong();
    private final LongAdder concurrentRequests = new LongAdder();

    public BookingService(BookingRepository bookingRepository, ResourceRepository resourceRepository, PricingService pricingService, AppConfig appConfig) {
        this.bookingRepository = bookingRepository;

        this.resourceRepository = resourceRepository;
        this.pricingService = pricingService;
        this.maxActiveBookingsPerUser = appConfig.getInt("booking.max.per.user", 5);
    }

    public BookingResult book(long userId, long resourceId, TimeSlot slot) {
        concurrentRequests.increment();

        try {
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

            try {
                Resource resource = resourceRepository.findById(resourceId);

                if (resource == null) {
                    throw new ResourceNotFoundException(resourceId);
                }

                if (resource.getStatus() != ResourceStatus.ACTIVE) {
                    throw new ResourceUnavailableException(resourceId, resource.getStatus());
                }

                if (!resource.getResourceType().canBeBookedFor(slot)) {
                    throw new SlotInvalidException(slot);
                }

                List<Booking> userBooking = findUserBooking(userId);
                long activeCount = countActive(userBooking);

                if (activeCount >= maxActiveBookingsPerUser) {
                    throw new BookingLimitExceededException(userId, maxActiveBookingsPerUser);
                }

                Booking conflict = findConflicts(resourceId, slot);
                if (conflict != null) {
                    rejectedBookings.incrementAndGet();
                    return new BookingResult.Conflict(conflict);
                }

                Money price = pricingService.calculatePrice(resource, slot);
                Booking booking = Booking.of(userId, resourceId, slot, price);
                bookingRepository.save(booking);
                totalBookings.incrementAndGet();

                return new BookingResult.Success(booking);
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

    public BookingResult cancel(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException(bookingId);
        }

        ReentrantLock lock = lockFor(booking.getResourceId());
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

        try {
            Booking current = bookingRepository.findById(bookingId);

            if (current == null) {
                throw new BookingNotFoundException(bookingId);
            }

            if (current.getUserId() != userId) {
                throw new CancellationNotAllowedException(bookingId);
            }

            if (current.getStatus() == BookingStatus.CANCELLED) {
                throw new CancellationNotAllowedException(bookingId);
            }

            // TODO: I7 — правило 2 часов, после этапа 11
            current.cancel();
            bookingRepository.save(current);

            return new BookingResult.Success(current);
        } finally {
            lock.unlock();
        }
    }

    public List<Booking> findUserBooking(long userId) {
        return bookingRepository.findByUserId(userId);
    }

    private long countActive(List<Booking> bookings) {
        long count = 0;

        for (Booking booking : bookings) {
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
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
