package com.igmiller.booking.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    private Money(BigDecimal amount, Currency currency) {
        this.amount = new BigDecimal(String.valueOf(amount)).setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    public static Money of(BigDecimal finalPrice) {
        return new Money(finalPrice, Currency.USD);
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money of(String amount, Currency currency) {
        if ((currency == null)) {
            throw new IllegalArgumentException("Currency must not be null");
        }

        if (amount == null || amount.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid amount!");
        }

        return new Money(new BigDecimal(amount), currency);
    }

    public static Money ofMinor(long minorUnits, Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }

        BigDecimal amount = BigDecimal.valueOf(minorUnits).movePointLeft(2);
        return new Money(amount, currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Money money = (Money) o;

        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }

    public BigDecimal toBigDecimal() {
        return new BigDecimal(amount.toString());
    }

    public Money add(Money other) {
        if (other.currency != this.currency) {
            throw new IllegalArgumentException("Please use currency you already selected: " + this.currency);
        }

        BigDecimal newAmount = this.amount.add(other.amount);
        return new Money(newAmount, currency);
    }

    public Money multiply(BigDecimal discountMultiplier) {
        return new Money(amount.multiply(discountMultiplier), currency);
    }
}
