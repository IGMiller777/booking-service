package com.igmiller.booking.domain;

public record ResourceUsage(String resourceCode, int bookingsCount, int busyMinutes) {
    public ResourceUsage {
        if(bookingsCount < 0) {
            throw new IllegalArgumentException("BookingsCount cannot be negative");
        }

        if(busyMinutes < 0) {
            throw new IllegalArgumentException("BusyMinutes cannot be negative");
        }
    }
}
