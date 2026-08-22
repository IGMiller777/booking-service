package com.igmiller.booking.domain;

public class User implements Identifiable<Long> {
    private static long nextId = 1;
    private final long id;
    private final String name;
    private final String email;
    private final Role role;

    private User(String name, String email, Role role) {
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
        this.role = role;
    }

    public static User of(String name, String email, Role role) {
        return new User(name, email, role);
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
