package com.igmiller.booking.domain;

import com.igmiller.booking.util.Validators;

import java.util.concurrent.atomic.AtomicLong;

public class User implements Identifiable<Long> {
    private static final AtomicLong nextId = new AtomicLong(1);
    private final long id;
    private final String name;
    private final String email;
    private final Role role;

    private User(long id, String name, String email, Role role) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email invalid format");
        }

        if(!Validators.isValidEmail(email)) {
            throw new IllegalArgumentException("Email invalid format");
        }

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static User restore(long id, String name, String email, Role role) {
        User user = new User(id, name, email, role);
        nextId.updateAndGet(current -> Math.max(current, id + 1));

        return user;
    }

    public static User of(String name, String email, Role role) {

        return new User(nextId.getAndIncrement(), name, email, role);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return id == user.id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
