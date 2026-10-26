package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceTypeTest {

    // I2: длительность не меньше 30 минут, значит минимум ниже 30 у типа ресурса недостижим
    @Test
    void minBookingMinutes_shouldNotBeBelowGlobalMinimum() {
        for (ResourceType type : ResourceType.values()) {
            assertTrue(type.getMinBookingMinutes() >= 30, type + " has unreachable minimum");
        }
    }
}
