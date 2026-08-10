package com.booking;

import com.booking.domain.Money;
import com.booking.domain.TimeSlot;

public class Booking {
    private static long nextId = 1;
    private final long id;
    private final long userId;
    private final long resourceId;
    private final TimeSlot slot;
    private final Money price;
    private boolean cancelled = false;

    public Booking(long userId, long resourceId, TimeSlot slot, Money price) {
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
    }

    public void cancel() {
        this.cancelled = true;
    }
}
