package com.igmiller.booking.domain;

import com.igmiller.booking.repository.Identifiable;

public class User implements Identifiable<Long> {
    private static long nextId = 1;
    private final long id;
    private final String name;
    private final String email;
    private final Role role;

    public User(String name, String email, Role role) {
        this.role = role;
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email invalid format");
        }

        id = nextId++;
        this.name = name;
        this.email = email;
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
}
