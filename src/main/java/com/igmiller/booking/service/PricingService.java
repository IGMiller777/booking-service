package com.igmiller.booking.service;

import com.igmiller.booking.domain.Money;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.TimeSlot;

public class PricingService {
    private static final int DISCOUNT_THRESHOLD_MINUTES = 4 * 60;

    private final PricingStrategy hourly = new HourlyPricingStrategy();
    private final PricingStrategy discounted = new DiscountedPricingStrategy();

    public Money calculatePrice(Resource resource, TimeSlot slot) {
        PricingStrategy strategy = chooseStrategy(slot);

        return strategy.calculate(resource, slot);
    }

    private PricingStrategy chooseStrategy(TimeSlot slot) {
        if (slot.durationMinutes() > DISCOUNT_THRESHOLD_MINUTES) {
            return discounted;
        }

        return hourly;
    }
}
