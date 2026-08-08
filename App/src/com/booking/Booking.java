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

    public Booking(int userId, int resourceId, TimeSlot slot, Money price) {
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
