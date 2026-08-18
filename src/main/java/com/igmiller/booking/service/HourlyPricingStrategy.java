package com.igmiller.booking.service;

import com.igmiller.booking.domain.Money;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.TimeSlot;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HourlyPricingStrategy implements PricingStrategy {

    @Override
    public Money calculate(Resource resource, TimeSlot timeSlot) {
        int rawMinutes = timeSlot.durationMinutes();
        int roundedMinutes = roundUpTo15(rawMinutes);

        BigDecimal hours = BigDecimal.valueOf(roundedMinutes).divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP);
        BigDecimal rawPrice = resource.getHourlyRate().toBigDecimal().multiply(hours);
        BigDecimal finalPrice = rawPrice.setScale(2, RoundingMode.HALF_UP);

        return Money.of(finalPrice);

    }

    private int roundUpTo15(int minutes) {
        int remain = minutes % 15;

        return remain == 0 ? minutes : minutes + (15 - remain);
    }
}
