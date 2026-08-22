package com.igmiller.booking.exception;

public class ResourceNotFoundException extends BookingServiceException {
    private final long resourceId;

    public ResourceNotFoundException(long resourceId) {
        super("Resource not found for id: " + resourceId);
        this.resourceId = resourceId;
    }

    public long getResourceId() {
        return resourceId;
    }
}
