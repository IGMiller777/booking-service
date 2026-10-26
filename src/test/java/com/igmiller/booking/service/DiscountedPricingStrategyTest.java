package com.igmiller.booking.service;

import com.igmiller.booking.domain.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountedPricingStrategyTest {
    private static final LocalDate DATE = LocalDate.of(2026, 11, 2);
    private final DiscountedPricingStrategy strategy = new DiscountedPricingStrategy();

    private static Resource roomWithRate(String rate, Currency currency) {
        return Resource.of("Room", "MR-101", 8, Money.of(rate, currency), ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);
    }

    @Test
    void calculate_shouldApplyTenPercentDiscount_andKeepCurrency() {
        // 5 ч * 100 = 500 -> 450
        Money price = strategy.calculate(roomWithRate("100", Currency.EUR), TimeSlot.ofHours(DATE, 9, 14));

        assertEquals(Money.of("450", Currency.EUR), price);
    }

    @Test
    void calculate_shouldRoundHalfUp_afterDiscount() {
        // 5 ч * 33.33 = 166.65 -> * 0.9 = 149.985 -> 149.99
        Money price = strategy.calculate(roomWithRate("33.33", Currency.USD), TimeSlot.ofHours(DATE, 9, 14));

        assertEquals(Money.of("149.99", Currency.USD), price);
    }
}
