package com.igmiller.booking.domain;

public class Booking implements Identifiable<Long> {
    private static long nextId = 1;
    private final long id;
    private final long userId;
    private final long resourceId;
    private final TimeSlot slot;
    private final Money price;
    private BookingStatus status;

    private Booking(long userId, long resourceId, TimeSlot slot, Money price) {
        if (slot == null) {
            throw new IllegalArgumentException("slot cannot be null");
        }

        if (price == null) {
            throw new IllegalArgumentException("price cannot be null");
        }

        if (resourceId <= 0 || userId <= 0) {
            throw new IllegalArgumentException("User Id or Resource Id cannot be null");
        }

        id = nextId++;
        this.userId = userId;
        this.resourceId = resourceId;
        this.slot = slot;
        this.price = price;
        this.status = BookingStatus.CONFIRMED;
    }

    public static Booking of(long userId, long resourceId, TimeSlot slot, Money price) {
        return new Booking(userId, resourceId, slot, price);
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

        Booking other = (Booking) o;

        return id == other.id;
    }


    @Override
    public String toString() {
        return "Bookings{id: %d, resourceId: %d, slot:%s, price:%s, status: %s}".formatted(id, resourceId, slot.toString(), price.toString(), status);
    }


    @Override
    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getResourceId() {
        return resourceId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }
}
