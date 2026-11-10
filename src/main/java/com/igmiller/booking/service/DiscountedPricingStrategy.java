package com.igmiller.booking.service;

import com.igmiller.booking.domain.Money;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.TimeSlot;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DiscountedPricingStrategy implements PricingStrategy {
    private static final BigDecimal DISCOUNT_MULTIPLIER = new BigDecimal("0.9"); // скидка 10%
    private final PricingStrategy base;

    public DiscountedPricingStrategy(HourlyPricingStrategy base) {
        this.base = base;
    }

    @Override
    public Money calculate(Resource resource, TimeSlot slot) {
        Money basePrice = base.calculate(resource, slot);
        return basePrice.multiply(DISCOUNT_MULTIPLIER);
    }
}
