package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceIdTest {

    private static Resource newResource() {
        return Resource.of("Room", "MR-101", 4, Money.of("100", Currency.USD),
                ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);
    }

    @Test
    void of_shouldGenerateUniqueIds_underConcurrentCalls() {
        Set<Long> ids = ConcurrentHashMap.newKeySet();
        int count = 2_000;

        IntStream.range(0, count).parallel().forEach(i -> ids.add(newResource().getId()));

        assertEquals(count, ids.size());
    }

    @Test
    void restore_shouldMoveCounterPastRestoredId() {
        long restoredId = newResource().getId() + 1_000;
        Resource.restore(restoredId, "Room", "MR-102", 4, Money.of("100", Currency.USD),
                ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);

        Resource next = newResource();

        assertTrue(next.getId() > restoredId);
    }
}
