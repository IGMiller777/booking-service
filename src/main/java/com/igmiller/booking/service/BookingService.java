package com.igmiller.booking.service;

import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.domain.*;
import com.igmiller.booking.exception.*;
import com.igmiller.booking.repository.Repository;

public class BookingService {

    private static final int MAX_ACTIVE_BOOKINGS_PER_USER = 5;

    private final Repository<Booking, Long> bookingRepository;
    private final Repository<Resource, Long> resourceRepository;
    private final PricingService pricingService;

    public BookingService(Repository<Booking, Long> bookingRepository, Repository<Resource, Long> resourceRepository, PricingService pricingService) {
        this.bookingRepository = bookingRepository;
        this.resourceRepository = resourceRepository;
        this.pricingService = pricingService;
    }

    public BookingResult book(long userId, long resourceId, TimeSlot slot) {
        Resource resource = resourceRepository.findById(resourceId);

        if (resource == null) {
            throw new ResourceNotFoundException(resourceId);
        }

        if (resource.getStatus() != ResourceStatus.ACTIVE) {
            throw new ResourceUnavailableException(resourceId, resource.getStatus());
        }

        if (resource.getResourceType() == ResourceType.MEETING_ROOM && slot.duration() < 30) {
            throw new ResourceUnavailableException(resourceId, resource.getStatus());
        }

        if (resource.getResourceType() == ResourceType.DESK && slot.duration() < 15) {
            throw new ResourceUnavailableException(resourceId, resource.getStatus());
        }

        Booking[] userBooking = findUserBooking(userId);
        long activeCount = countActive(userBooking);

        if (activeCount >= MAX_ACTIVE_BOOKINGS_PER_USER) {
            throw new BookingLimitExceededException(userId, MAX_ACTIVE_BOOKINGS_PER_USER);
        }

        Booking conflict = findConflicts(resourceId, slot);
        if (conflict != null) {
            return new BookingResult.Conflict(conflict);
        }

        Money price = pricingService.calculatePrice(resource, slot);
        Booking booking = Booking.of(userId, resourceId, slot, price);
        bookingRepository.save(booking);

        return new BookingResult.Success(booking);

    }

    public BookingResult cancel(User user, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException(bookingId);
        }

        if (booking.getUserId() != user.getId()) {
            throw new CancellationNotAllowedException(bookingId);
        }

        if (booking.getStatus() != BookingStatus.CANCELLED) {
            throw new CancellationNotAllowedException(bookingId);
        }

        // TODO: I7 — правило 2 часов, после этапа 11
        booking.cancel();
        bookingRepository.save(booking);

        return new BookingResult.Success(booking);
    }

    public Booking[] findUserBooking(long userId) {
        Booking[] all = bookingRepository.findAll();
        int count = 0;

        for (Booking booking : all) {
            if (booking.getUserId() == userId) {
                count++;
            }
        }

        Booking[] result = new Booking[count];
        int index = 0;
        for (Booking booking : all) {
            if (booking.getUserId() == userId) {
                result[index++] = booking;
            }
        }

        return result;
    }

    private long countActive(Booking[] bookings) {
        long count = 0;

        for (Booking booking : bookings) {
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                count++;
            }
        }

        return count;
    }

    private Booking findConflicts(long resourceId, TimeSlot slot) {
        Booking[] all = bookingRepository.findAll();
        for (Booking booking : all) {
            if (booking.getResourceId() == resourceId && booking.getStatus() == BookingStatus.CONFIRMED && booking.getSlot().overlaps(slot)) {
                return booking;
            }
        }

        return null;
    }
}
