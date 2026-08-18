package com.igmiller.booking.service;

import com.igmiller.booking.domain.Money;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.TimeSlot;

public interface PricingStrategy {
    Money calculate(Resource resource, TimeSlot timeSlot);
}
