package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Booking;

public class InMemoryBookingsRepository implements Repository<Booking, Long> {
    private Object[] items = new Object[10];
    private int size = 0;

    @Override
    public Booking save(Booking entity) {
        for (int i = 0; i < size; i++) {
            Booking existing = (Booking) items[i];

            if (existing.getId().equals(entity.getId())) {
                items[i] = entity;

                return entity;
            }
        }

        if (size == items.length) {
            grow();
        }

        items[size] = entity;
        size++;

        return entity;
    }

    @Override
    public Booking findById(Long id) {
        for (Object item : items) {
            Booking existing = (Booking) item;
            if (existing.getId().equals(id)) {
                return existing;
            }
        }

        return null;
    }

    @Override
    public Booking[] findAll() {
        Booking[] results = new Booking[size];
        for (int i = 0; i < size; i++) {
            results[i] = (Booking) items[i];
        }

        return results;
    }

    @Override
    public boolean delete(Long id) {
        for (int i = 0; i < size; i++) {
            Booking existing = (Booking) items[i];

            if (existing.getId().equals(id)) {
                for (int j = i; j < size - 1; j++) {
                    items[j] = items[j + 1];
                }

                items[size - 1] = null;
                size--;
                return true;
            }
        }

        return false;
    }

    public void grow() {
        items = java.util.Arrays.copyOf(items, items.length * 2);
    }
}
