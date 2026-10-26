package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void of_shouldRejectNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of((BigDecimal) null, Currency.USD));
    }

    @Test
    void of_shouldRejectNullCurrency() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(BigDecimal.TEN, null));
    }

    @Test
    void of_shouldRejectNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("-0.01", Currency.USD));
        assertThrows(IllegalArgumentException.class, () -> Money.ofMinor(-1, Currency.USD));
    }

    @Test
    void of_shouldAcceptZero() {
        assertEquals(new BigDecimal("0.00"), Money.of("0", Currency.USD).getAmount());
    }

    @Test
    void of_shouldRoundHalfUpToTwoDecimals() {
        assertEquals(new BigDecimal("1.01"), Money.of("1.005", Currency.USD).getAmount());
    }
}
