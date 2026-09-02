package com.igmiller.booking.domain;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

public class Booking implements Identifiable<Long> {
    private static final AtomicLong nextId = new AtomicLong(1);
    private final long id;
    private final long userId;
    private final long resourceId;
    private final TimeSlot slot;
    private final Money price;
    private BookingStatus status;

    private Booking(long id, long userId, long resourceId, TimeSlot slot, Money price, BookingStatus status) {
        if (slot == null) {
            throw new IllegalArgumentException("Slot cannot be null");
        }

        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        if (resourceId <= 0 || userId <= 0) {
            throw new IllegalArgumentException("User Id or Resource Id cannot be null");
        }

        this.id = id;
        this.userId = userId;
        this.resourceId = resourceId;
        this.slot = slot;
        this.price = price;
        this.status = status;
    }

    public static Booking of(long userId, long resourceId, TimeSlot slot, Money price) {
        return new Booking(nextId.getAndIncrement(), userId, resourceId, slot, price, BookingStatus.CONFIRMED);
    }

    public static Booking restore(long id, long userId, long resourceId, TimeSlot slot, Money price, BookingStatus status) {
        Booking booking = new Booking(id, userId, resourceId, slot, price, status);
        nextId.updateAndGet(current -> Math.max(current, id + 1));

        return booking;
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
        return "Booking{id: %d, resourceId: %d, slot: %s, price: %s, status: %s}"
                .formatted(id, resourceId, slot, price, status);
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

    public LocalDate getDate() {
        return slot.getDate();
    }

    public BookingStatus getStatus() {
        return status;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public Money getPrice() {
        return price;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }
}
