package com.booking;

import com.booking.domain.Money;
import com.money.MyCurrency;

public class BookingApplication {
    //    TemperatureConverter.main(args);

    Money a = Money.of("10.00", MyCurrency.EUR);
    Money b = a.add(Money.of("5.00", MyCurrency.EUR));
}
