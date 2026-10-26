package com.igmiller.booking.service;

import com.igmiller.booking.domain.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HourlyPricingStrategyTest {

    @Test
    void calculate_shouldKeepCurrencyOfResourceRate() {
        Resource room = Resource.of("Room", "MR-101", 8,
                Money.of("1500", Currency.RUB), ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);
        TimeSlot slot = TimeSlot.ofHours(LocalDate.now().plusDays(1), 10, 12);

        Money price = new HourlyPricingStrategy().calculate(room, slot);

        assertEquals(Money.of("3000", Currency.RUB), price);
    }
}
