package com.igmiller.booking.domain;

import java.util.Comparator;

public final class ResourceComparators {
    public static final Comparator<Resource> BY_NAME = Comparator.comparing(Resource::getName);

    public static final Comparator<Resource> BY_CAPACITY_DESC = Comparator.comparingInt(Resource::getCapacity).reversed();

    public static final Comparator<Resource> BY_PRICE_THEN_NAME = Comparator.comparing((Resource r) -> r.getHourlyRate().getAmount()).thenComparing(Resource::getName);
}
