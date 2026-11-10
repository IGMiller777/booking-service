package com.igmiller.booking.domain;

import java.util.Objects;

public class Resource implements Identifiable<Long> {
    private final Long id;
    private final String name;
    private final String code;
    private final int capacity;
    private final Money hourlyRate;
    private ResourceStatus resourceStatus = ResourceStatus.ACTIVE;
    private ResourceType resourceType = ResourceType.MEETING_ROOM;

    private Resource(Long id, String name, String code, int capacity, Money hourlyRate, ResourceType resourceType, ResourceStatus resourceStatus) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("Id must be a positive integer");
        }

        if (name == null || code == null || hourlyRate == null || name.isEmpty() || code.isEmpty()) {
            throw new IllegalArgumentException("Name and code cannot be empty");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        if (!Validators.isValidResourceCode(code)) {
            throw new IllegalArgumentException("Invalid resource code");
        }

        this.id = id;
        this.name = name;
        this.code = code;
        this.capacity = capacity;
        this.hourlyRate = hourlyRate;
        this.resourceType = resourceType;
        this.resourceStatus = resourceStatus;
    }

    public static Resource restore(long id, String name, String code, int capacity, Money hourlyRate, ResourceType resourceType, ResourceStatus status) {
        return new Resource(id, name, code, capacity, hourlyRate, resourceType, status);
    }

    public Resource withId(Long id) {
        return restore(id, name, code, capacity, hourlyRate, resourceType, resourceStatus);
    }

    public static Resource of(String name, String code, int capacity, Money hourlyRate, ResourceType resourceType, ResourceStatus status) {
        return new Resource(null, name, code, capacity, hourlyRate, resourceType, status);
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

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Resource{code='%s', name='%s'; capacity=%d}".formatted(code, name, capacity);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Money getHourlyRate() {
        return hourlyRate;
    }

    public ResourceStatus getStatus() {
        return resourceStatus;
    }

    public void activate() {
        requireNotRetired("activated");
        this.resourceStatus = ResourceStatus.ACTIVE;
    }

    public void startMaintenance() {
        requireNotRetired("moved to maintenance");
        this.resourceStatus = ResourceStatus.MAINTENANCE;
    }

    public void retire() {
        this.resourceStatus = ResourceStatus.RETIRED;
    }

    private void requireNotRetired(String action) {
        if (this.resourceStatus == ResourceStatus.RETIRED) {
            throw new IllegalStateException("Retired resource " + code + " cannot be " + action);
        }
    }
}
