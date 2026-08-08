package com.booking.domain;

public class User {
    private static long nextId = 1;
    private final long id;
    private final String name;
    private final String email;

    private User(String name, String email) {
        if(name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if(email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if(!email.contains("@")) {
            throw new IllegalArgumentException("Email invalid format");
        }

        id = nextId++;
        this.name = name;
        this.email = email;
    }
}
