package com.igmiller.booking.repository.file;

import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.repository.BookingsRepository;
import com.igmiller.booking.repository.Repository;

import java.util.List;

public class FileBookingRepository implements Repository<Booking, Long> {
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

    public List<Booking> findByUserId(long userId) {
        return cache.findByUserId(userId);
    }

    public List<Booking> findByResourceId(long resourceId) {
        return cache.findByResourceId(resourceId);
    }

    private void persist() {
        storage.saveAll(cache.findAll());
    }
}
