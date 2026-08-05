package com.booking.domain;

import com.money.MyCurrency;

import java.math.BigDecimal;

public final class Money {
    private final BigDecimal amount;
    private final MyCurrency currency;

    private Money(BigDecimal amount, MyCurrency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MyCurrency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }

    public Money add(Money other) {
        if (other.currency != this.currency) {
            throw new IllegalArgumentException("Please use currency you already selected: " + this.currency);
        }

        BigDecimal newAmount = this.amount.add(other.amount);
        return new Money(newAmount, currency);
    }

    public static Money of(String amount, MyCurrency currency) {
        if((currency == null)) {
            throw new IllegalArgumentException("Currency must not be null");
        }

        if (amount == null || amount.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid amount!");
        }

        return new Money(new BigDecimal(amount), currency);
    }

    public static Money ofMinor(long minorUnits, MyCurrency currency) {
        if(currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }

        BigDecimal amount = BigDecimal.valueOf(minorUnits).movePointLeft(2);
        return new Money(amount, currency);
    }
}
