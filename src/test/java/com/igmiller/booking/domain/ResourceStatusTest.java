package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceStatusTest {

    private Resource active() {
        return Resource.of("Room", "MR-101", 4, Money.of("100", Currency.USD),
                ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);
    }

    @Test
    void startMaintenance_shouldMoveActiveToMaintenance_andActivateShouldReturnIt() {
        Resource resource = active();

        resource.startMaintenance();
        assertEquals(ResourceStatus.MAINTENANCE, resource.getStatus());

        resource.activate();
        assertEquals(ResourceStatus.ACTIVE, resource.getStatus());
    }

    @Test
    void retire_shouldBeTerminal() {
        Resource resource = active();

        resource.retire();

        assertEquals(ResourceStatus.RETIRED, resource.getStatus());
        assertThrows(IllegalStateException.class, resource::activate);
        assertThrows(IllegalStateException.class, resource::startMaintenance);
        assertEquals(ResourceStatus.RETIRED, resource.getStatus());
    }
}
