package com.igmiller.booking.domain;

import java.time.LocalDate;

public class Booking implements Identifiable<Long> {
    private static long nextId = 1;
    private final long id;
    private final long userId;
    private final long resourceId;
    private final LocalDate date;
    private final TimeSlot slot;
    private final Money price;
    private BookingStatus status;

    private Booking(long id, long userId, long resourceId, LocalDate date, TimeSlot slot, Money price, BookingStatus status) {
        if (slot == null) {
            throw new IllegalArgumentException("slot cannot be null");
        }

        if (price == null) {
            throw new IllegalArgumentException("price cannot be null");
        }

        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (resourceId <= 0 || userId <= 0) {
            throw new IllegalArgumentException("User Id or Resource Id cannot be null");
        }

        this.id = id;
        this.userId = userId;
        this.resourceId = resourceId;
        this.date = date;
        this.slot = slot;
        this.price = price;
        this.status = status;
    }

    public static Booking of(long userId, long resourceId, LocalDate date, TimeSlot slot, Money price) {
        Booking booking = new Booking(nextId, userId, resourceId, date, slot, price, BookingStatus.CONFIRMED);
        nextId++;

        return booking;
    }

    public static Booking restore(long id, long userId, long resourceId, LocalDate date, TimeSlot slot, Money price, BookingStatus status) {
        Booking booking = new Booking(id, userId, resourceId, date, slot, price, status);
        if (id >= nextId) {
            nextId = id + 1;
        }

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
        return "Booking{id: %d, resourceId: %d, date: %s, slot: %s, price: %s, status: %s}"
                .formatted(id, resourceId, date, slot, price, status);
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
        return date;
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
