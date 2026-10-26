package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Booking;

import java.util.*;

public class InMemoryBookingRepository implements BookingRepository {
    private final Map<Long, Booking> bookingsById = new HashMap<>();
    private final Map<Long, Set<Booking>> bookingsByUserId = new HashMap<>();
    private final Map<Long, Set<Booking>> bookingsByResourceId = new HashMap<>();

    @Override
    public Booking save(Booking booking) {
        Booking previous = bookingsById.put(booking.getId(), booking);
        if (previous != null) {
            removeFromIndexes(previous);
        }

        bookingsByUserId.computeIfAbsent(booking.getUserId(), id -> new LinkedHashSet<>()).add(booking);
        bookingsByResourceId.computeIfAbsent(booking.getResourceId(), id -> new LinkedHashSet<>()).add(booking);

        return booking;
    }

    @Override
    public Booking findById(Long id) {
        return bookingsById.get(id);
    }

    @Override
    public List<Booking> findByUserId(Long userId) {
        return new ArrayList<>(bookingsByUserId.getOrDefault(userId, Set.of()));
    }

    @Override
    public List<Booking> findByResourceId(Long resourceId) {
        return new ArrayList<>(bookingsByResourceId.getOrDefault(resourceId, Set.of()));
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<Booking>(bookingsById.values());
    }

    @Override
    public boolean delete(Long id) {
        Booking booking = bookingsById.remove(id);
        if (booking == null) {
            return false;
        }

        removeFromIndexes(booking);

        return true;
    }

    private void removeFromIndexes(Booking booking) {
        Set<Booking> userBookings = bookingsByUserId.get(booking.getUserId());
        if(userBookings != null) {
            userBookings.remove(booking);
        }

        Set<Booking> resourceBookings = bookingsByResourceId.get(booking.getResourceId());
        if(resourceBookings != null) {
            resourceBookings.remove(booking);
        }
    }
}
