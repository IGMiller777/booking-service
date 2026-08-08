package com.booking.domain;

public class Resource {
    private static long nextId = 1;
    private final long id;
    private final String name;
    private final String code;
    private final int capacity;
    private final Money hourlyRate;
    private boolean active;

    private Resource(String name, String code, int capacity, Money hourlyRate) {
        if(name.isEmpty() || code.isEmpty()) {
            throw new IllegalArgumentException("Name and code cannot be empty");
        }

        if(capacity == 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        id = nextId++;
        this.name = name;
        this.code = code;
        this.capacity = capacity;
        this.hourlyRate = hourlyRate;
    }

    public void activate(boolean active) {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
