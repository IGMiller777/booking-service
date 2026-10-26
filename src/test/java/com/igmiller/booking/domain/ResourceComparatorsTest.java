package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceComparatorsTest {

    private Resource resource(String name, String code, String rate, Currency currency) {
        return Resource.of(name, code, 4, Money.of(rate, currency), ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE);
    }

    @Test
    void byPriceThenName_shouldNotMixCurrencies() {
        Resource usd200 = resource("B", "MR-201", "200", Currency.USD);
        Resource rub100 = resource("A", "MR-202", "100", Currency.RUB);
        Resource usd100 = resource("C", "MR-203", "100", Currency.USD);
        List<Resource> list = new ArrayList<>(List.of(usd200, rub100, usd100));

        list.sort(ResourceComparators.BY_PRICE_THEN_NAME);

        // USD(100), USD(200) идут подряд, RUB(100) не оказывается "дешевле" USD(200)
        assertEquals(List.of(usd100, usd200, rub100), list);
    }

    @Test
    void byPriceThenName_shouldUseNameAsTieBreaker() {
        Resource b = resource("B", "MR-301", "100", Currency.USD);
        Resource a = resource("A", "MR-302", "100", Currency.USD);
        List<Resource> list = new ArrayList<>(List.of(b, a));

        list.sort(ResourceComparators.BY_PRICE_THEN_NAME);

        assertEquals(List.of(a, b), list);
    }
}
