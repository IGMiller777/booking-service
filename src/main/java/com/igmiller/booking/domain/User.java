package com.igmiller.booking.domain;

import java.util.Objects;

public class User implements Identifiable<Long> {
    private final Long id;
    private final String name;
    private final String email;
    private final Role role;

    private User(Long id, String name, String email, Role role) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("Id must be a positive integer");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!Validators.isValidEmail(email)) {
            throw new IllegalArgumentException("Email invalid format");
        }

        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static User restore(long id, String name, String email, Role role) {

        return new User(id, name, email, role);
    }

    public User withId(Long id) {
        return restore(id, name, email, role);
    }

    public static User of(String name, String email, Role role) {
        return new User(null, name, email, role);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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
        return id != null && id.equals(user.id);
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
