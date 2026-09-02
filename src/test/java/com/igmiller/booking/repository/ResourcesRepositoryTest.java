package com.igmiller.booking.repository;

import com.igmiller.booking.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourcesRepositoryTest {
    private ResourcesRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ResourcesRepository();
    }

    @Test
    void save_shouldMakeEntityFindableById_whenEntityIsNew() {
        Resource resource = Resource.of("Room 1", "CODE-1", 8, Money.of("1000.0", Currency.EUR), ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);
        repository.save(resource);

        assertEquals(resource, repository.findById(resource.getId()));
    }

}
