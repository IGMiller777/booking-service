package com.igmiller.booking.repository;

import com.igmiller.booking.domain.User;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {
    private final List<User> items = new ArrayList<User>();
    private long nextId = 1;

    @Override
    public User save(User entity) {
        User user = entity.getId() == null ? entity.withId(nextId++) : entity;
        nextId = Math.max(nextId, user.getId() + 1);

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(entity.getId())) {
                items.set(i, user);

                return user;
            }
        }

        items.add(user);

        return user;
    }

    @Override
    public User findById(Long id) {
        for (User user : items) {
            if (user.getId().equals(id)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<User>(items);
    }

    @Override
    public boolean delete(Long id) {
        return items.removeIf(user -> user.getId().equals(id));
    }
}
