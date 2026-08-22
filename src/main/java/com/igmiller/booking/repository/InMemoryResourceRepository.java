package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Money;
import com.igmiller.booking.domain.MyCurrency;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.ResourceType;

public class InMemoryResourceRepository implements Repository<Resource, Long> {
    private Object[] items = new Object[10];
    private int size = 0;

    public InMemoryResourceRepository() {
        this.items[0] = Resource.of("RESOURCE", "BLACK", 5, Money.of("100", MyCurrency.EUR), ResourceType.MEETING_ROOM);
    }

    @Override
    public Resource save(Resource entity) {
        for (int i = 0; i < size; i++) {
            Resource existing = (Resource) items[i];

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
    public Resource findById(Long id) {
        for (Object item : items) {
            Resource existing = (Resource) item;
            if (existing.getId().equals(id)) {
                return existing;
            }
        }

        return null;
    }

    @Override
    public Resource[] findAll() {
        Resource[] results = new Resource[size];
        for (int i = 0; i < size; i++) {
            results[i] = (Resource) items[i];
        }

        return results;
    }

    @Override
    public boolean delete(Long id) {
        for (int i = 0; i < size; i++) {
            Resource existing = (Resource) items[i];

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

    private void grow() {
        items = java.util.Arrays.copyOf(items, items.length * 2);
    }
}
