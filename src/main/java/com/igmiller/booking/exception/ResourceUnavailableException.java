package com.igmiller.booking.exception;

import com.igmiller.booking.domain.ResourceStatus;

public class ResourceUnavailableException extends BookingServiceException {
    private final long resourceId;
    private final ResourceStatus status;

    public ResourceUnavailableException(long resourceId, ResourceStatus status) {
        super("Resource " + resourceId + " is unavailable. Status " + status);
        this.resourceId = resourceId;
        this.status = status;
    }

    public long getResourceId() {
        return resourceId;
    }

    public ResourceStatus getStatus() {
        return status;
    }
}
