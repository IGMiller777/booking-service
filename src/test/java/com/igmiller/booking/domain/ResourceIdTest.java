package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceIdTest {

    private static Resource newResource() {
        return Resource.of("Room", "MR-101", 4, Money.of("100", Currency.USD),
                ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);
    }

    @Test
    void of_shouldCreateResourceWithoutId() {
        assertNull(newResource().getId());
    }

    @Test
    void equals_shouldNotTreatTwoUnsavedResourcesAsEqual() {
        assertNotEquals(newResource(), newResource());
    }

    @Test
    void equals_shouldCompareById_whenIdsAreSet() {
        Resource saved = newResource().withId(7L);
        Resource sameIdOtherName = Resource.restore(7L, "Other", "MR-102", 2, Money.of("50", Currency.USD),
                ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);

        assertEquals(saved, sameIdOtherName);
        assertEquals(saved.hashCode(), sameIdOtherName.hashCode());
    }

    @Test
    void withId_shouldReturnCopyWithId_andKeepOriginalUntouched() {
        Resource unsaved = newResource();

        Resource saved = unsaved.withId(7L);

        assertNull(unsaved.getId());
        assertEquals(7L, saved.getId());
        assertEquals(unsaved.getCode(), saved.getCode());
    }

    @Test
    void restore_shouldRejectNonPositiveId() {
        assertThrows(IllegalArgumentException.class, () -> newResource().withId(0L));
    }
}