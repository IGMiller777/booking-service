package com.igmiller.booking.domain;

public record BookingView(long id, String resourceName, String timeRange, String price, String status) {
}
