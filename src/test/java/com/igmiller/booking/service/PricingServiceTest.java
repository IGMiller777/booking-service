package com.igmiller.booking.service;

import com.igmiller.booking.domain.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceTest {
    private static final LocalDate DATE = LocalDate.of(2026, 11, 2);
    private final PricingService pricing = new PricingService();
    private final Resource room = Resource.of("Room", "MR-101", 8, Money.of("100", Currency.USD),
            ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);

    @Test
    void calculatePrice_shouldNotDiscount_whenExactlyFourHours() {
        Money price = pricing.calculatePrice(room, TimeSlot.ofHours(DATE, 9, 13));

        assertEquals(Money.of("400", Currency.USD), price);
    }

    @Test
    void calculatePrice_shouldDiscount_whenLongerThanFourHours() {
        // 4 ч 15 мин * 100 = 425 -> * 0.9 = 382.50
        Money price = pricing.calculatePrice(room, TimeSlot.of(DATE, LocalTime.of(9, 0), LocalTime.of(13, 15)));

        assertEquals(Money.of("382.50", Currency.USD), price);
    }
}
