package com.igmiller.booking.service;

import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.domain.*;
import com.igmiller.booking.exception.*;
import com.igmiller.booking.repository.BookingRepository;
import com.igmiller.booking.repository.BookingsRepository;
import com.igmiller.booking.repository.Repository;
import com.igmiller.booking.repository.ResourceRepository;
import com.igmiller.booking.repository.file.FileBookingRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class BookingService {

    private static final int MAX_ACTIVE_BOOKINGS_PER_USER = 5;

    private final BookingRepository bookingRepository;
    private final ResourceRepository resourceRepository;
    private final PricingService pricingService;

    public BookingService(BookingRepository bookingRepository, ResourceRepository resourceRepository, PricingService pricingService) {
        this.bookingRepository = bookingRepository;

        this.resourceRepository = resourceRepository;
        this.pricingService = pricingService;
    }

    public BookingResult book(long userId, long resourceId, LocalDate date, TimeSlot slot) {
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

        if (activeCount >= MAX_ACTIVE_BOOKINGS_PER_USER) {
            throw new BookingLimitExceededException(userId, MAX_ACTIVE_BOOKINGS_PER_USER);
        }

        Booking conflict = findConflicts(resourceId, date, slot);
        if (conflict != null) {
            return new BookingResult.Conflict(conflict);
        }

        Money price = pricingService.calculatePrice(resource, slot);
        Booking booking = Booking.of(userId, resourceId, date, slot, price);
        bookingRepository.save(booking);

        return new BookingResult.Success(booking);

    }

    public BookingResult cancel(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException(bookingId);
        }

        if (booking.getUserId() != userId) {
            throw new CancellationNotAllowedException(bookingId);
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new CancellationNotAllowedException(bookingId);
        }

        // TODO: I7 — правило 2 часов, после этапа 11
        booking.cancel();
        bookingRepository.save(booking);

        return new BookingResult.Success(booking);
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

    private Booking findConflicts(long resourceId, LocalDate date, TimeSlot slot) {
        List<Booking> all = bookingRepository.findByResourceId(resourceId);
        for (Booking booking : all) {
            if (booking.getStatus() == BookingStatus.CONFIRMED && booking.getDate().equals(date) && booking.getSlot().overlaps(slot)) {
                return booking;
            }
        }

        return null;
    }
}
