package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Role;
import com.igmiller.booking.domain.User;

public class InMemoryUserRepository implements Repository<User, Long> {
    private Object[] items = new Object[10];
    private int size = 10;

    public InMemoryUserRepository() {
        items[0] = new User("Super Ivan", "super@admin.com", Role.USER);
    }

    @Override
    public User save(User entity) {
        for (int i = 0; i < items.length; i++) {
            Identifiable<Long> existing = (Identifiable<Long>) items[i];

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
    public User findById(Long id) {
        if (items[0] == null) {
            return null;
        }

        for (Object item : items) {
            User existing = (User) item;
            if (existing.getId() == id) {
                return existing;
            }
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public User[] findAll() {
        User[] results = new User[size];
        for (int i = 0; i < items.length; i++) {
            results[i] = (User) items[i];
        }

        return results;
    }

    @Override
    public boolean delete(Long id) {
        for (int i = 0; i < size; i++) {
            User existing = (User) items[i];

            if (existing.getId() == id) {
                for (int j = i; j < size; j++) {
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
