package com.igmiller.booking.service;

import com.igmiller.booking.domain.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HourlyPricingStrategyTest {
    private static final LocalDate DATE = LocalDate.of(2026, 11, 2);
    private final HourlyPricingStrategy strategy = new HourlyPricingStrategy();

    private static Resource roomWithRate(String rate, Currency currency) {
        return Resource.of("Room", "MR-101", 8, Money.of(rate, currency), ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);
    }

    private static TimeSlot slot(int fromHour, int fromMinute, int toHour, int toMinute) {
        return TimeSlot.of(DATE, LocalTime.of(fromHour, fromMinute), LocalTime.of(toHour, toMinute));
    }

    @Test
    void calculate_shouldKeepCurrencyOfResourceRate() {
        Money price = strategy.calculate(roomWithRate("1500", Currency.RUB), slot(10, 0, 12, 0));

        assertEquals(Money.of("3000", Currency.RUB), price);
    }

    @Test
    void calculate_shouldChargeForQuarterHours() {
        // 1 ч 45 мин = 1.75 ч
        Money price = strategy.calculate(roomWithRate("10", Currency.USD), slot(10, 0, 11, 45));

        assertEquals(Money.of("17.50", Currency.USD), price);
    }

    @Test
    void calculate_shouldRoundHalfUp_notHalfEven() {
        // I6: 33.33 * 0.5 = 16.665 -> HALF_UP даёт 16.67, HALF_EVEN дал бы 16.66
        Money price = strategy.calculate(roomWithRate("33.33", Currency.USD), slot(10, 0, 10, 30));

        assertEquals(Money.of("16.67", Currency.USD), price);
    }

    @Test
    void calculate_shouldRoundToKopecks_whenProductHasMoreDigits() {
        // 100.01 * 0.75 = 75.0075 -> 75.01
        Money price = strategy.calculate(roomWithRate("100.01", Currency.RUB), slot(10, 0, 10, 45));

        assertEquals(Money.of("75.01", Currency.RUB), price);
    }
}
