package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Money;
import com.igmiller.booking.domain.Currency;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.ResourceType;
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
        Resource resource = Resource.of("Room 1", "CODE-1", 8, Money.of("1000.0", Currency.EUR), ResourceType.MEETING_ROOM);
        repository.save(resource);

        assertEquals(resource, repository.findById(resource.getId()));
    }

}
