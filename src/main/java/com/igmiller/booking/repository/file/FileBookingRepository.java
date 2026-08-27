package com.igmiller.booking.repository.file;

import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.repository.BookingRepository;
import com.igmiller.booking.repository.BookingsRepository;

import java.util.List;

public class FileBookingRepository implements BookingRepository {
    private final Storage<Booking> storage;
    private final BookingsRepository cache;

    public FileBookingRepository(Storage<Booking> storage) {
        this.storage = storage;
        this.cache = new BookingsRepository();

        for (Booking booking : storage.loadAll()) {
            cache.save(booking);
        }
    }

    @Override
    public Booking save(Booking booking) {
        Booking saved = cache.save(booking);
        persist();
        return saved;
    }

    @Override
    public boolean delete(Long id) {
        boolean deleted = cache.delete(id);
        persist();
        return deleted;
    }

    @Override
    public Booking findById(Long id) {
        return cache.findById(id);
    }

    @Override
    public List<Booking> findAll() {
        return cache.findAll();
    }

    @Override
    public List<Booking> findByUserId(Long userId) {
        return cache.findByUserId(userId);
    }

    @Override
    public List<Booking> findByResourceId(Long resourceId) {
        return cache.findByResourceId(resourceId);
    }

    private void persist() {
        storage.saveAll(cache.findAll());
    }
}
