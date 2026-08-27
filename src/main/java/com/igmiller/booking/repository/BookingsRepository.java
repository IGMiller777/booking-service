package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingsRepository implements BookingRepository {
    private final Map<Long, Booking> bookingsById = new HashMap<>();
    private final Map<Long, List<Booking>> bookingsByUserId = new HashMap<>();
    private final Map<Long, List<Booking>> bookingsByResourceId = new HashMap<>();

    @Override
    public Booking save(Booking booking) {
        bookingsById.put(booking.getId(), booking);

        bookingsByUserId.computeIfAbsent(booking.getUserId(), id -> new ArrayList<>()).add(booking);
        bookingsByResourceId.computeIfAbsent(booking.getResourceId(), id -> new ArrayList<>()).add(booking);

        return booking;
    }

    @Override
    public Booking findById(Long id) {
        return bookingsById.get(id);
    }

    @Override
    public List<Booking> findByUserId(Long userId) {
        return bookingsByUserId.get(userId);
    }

    @Override
    public List<Booking> findByResourceId(Long resourceId) {
        return bookingsByResourceId.get(resourceId);
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<Booking>(bookingsById.values());
    }

    @Override
    public boolean delete(Long id) {

        Booking booking = bookingsById.get(id);
        if (booking == null) {
            return false;
        }

        bookingsById.remove(id);

        List<Booking> userBookings = bookingsByUserId.get(booking.getUserId());
        if (userBookings != null) {
            userBookings.remove(booking);
        }

        List<Booking> resourceBookings = bookingsByResourceId.get(booking.getResourceId());

        if (resourceBookings != null) {
            resourceBookings.remove(booking);
        }

        return true;
    }
}
