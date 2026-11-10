package com.igmiller.booking.service;

import com.igmiller.booking.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PricingWiringTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(
            HourlyPricingStrategy.class,
            DiscountedPricingStrategy.class,
            PricingService.class
    );

    @Test
    void calculate_price_should_wire_discount() {
        contextRunner.run(context -> {
            PricingService pricingService = context.getBean(PricingService.class);
            Resource room = Resource.of("Room", "MR-101", 8, Money.of("100", Currency.USD), ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);
            Money price = pricingService.calculatePrice(room, TimeSlot.ofHours(LocalDate.of(2026, 11, 2), 9, 14));

            assertEquals(Money.of("450", Currency.USD), price);
        });
    }
}
