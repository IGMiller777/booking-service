package com.igmiller.booking.service;

import com.igmiller.booking.domain.Money;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.TimeSlot;
import org.springframework.stereotype.Service;

@Service
public class PricingService {
    private static final int DISCOUNT_THRESHOLD_MINUTES = 4 * 60;

    private final PricingStrategy hourly;
    private final DiscountedPricingStrategy discounted;

    public PricingService(HourlyPricingStrategy hourly, DiscountedPricingStrategy discounted) {
        this.discounted = discounted;
        this.hourly = hourly;
    }

    public Money calculatePrice(Resource resource, TimeSlot slot) {
        PricingStrategy strategy = chooseStrategy(slot);

        return strategy.calculate(resource, slot);
    }

    private PricingStrategy chooseStrategy(TimeSlot slot) {
        if (slot.duration().toMinutes() > DISCOUNT_THRESHOLD_MINUTES) {
            return discounted;
        }

        return hourly;
    }
}
