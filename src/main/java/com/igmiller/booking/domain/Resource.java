package com.igmiller.booking.domain;

import com.igmiller.booking.repository.Identifiable;

import java.util.Objects;

public class Resource implements Identifiable<Long> {
    private static long nextId = 1;
    private final long id;
    private final String name;
    private final String code;
    private final int capacity;
    private final Money hourlyRate;
    private ResourceStatus resourceStatus;

    public Resource(String name, String code, int capacity, Money hourlyRate) {
        if (name.isEmpty() || code.isEmpty()) {
            throw new IllegalArgumentException("Name and code cannot be empty");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        id = nextId++;
        this.name = name;
        this.code = code;
        this.capacity = capacity;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource other = (Resource) o;

        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Resource{code='%s', name='%s'; capacity=%d}".formatted(code, name, capacity);
    }

    public void activate() {
        if (this.resourceStatus == ResourceStatus.RETIRED || this.resourceStatus == ResourceStatus.MAINTENANCE) {
            this.resourceStatus = ResourceStatus.ACTIVE;
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    public Money getHourlyRate() {
        return hourlyRate;
    }

    public ResourceStatus getStatus() {
        return resourceStatus;
    }

    public void deactivate() {
        this.resourceStatus = ResourceStatus.RETIRED;
    }
}
