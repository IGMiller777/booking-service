package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Booking;

import java.util.List;

public interface BookingRepository extends Repository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    List<Booking> findByResourceId(Long resourceId);
}
