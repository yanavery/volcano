//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//=================================================================================================
@Getter
@Component
public class Configuration {
    @Value("${volcano.booking.min.lead.time.days}")
    private int minLeadTimeDays;

    @Value("${volcano.booking.max.lead.time.days}")
    private int maxLeadTimeDays;

    @Value("${volcano.booking.min.duration.days}")
    private int minDurationDays;

    @Value("${volcano.booking.max.duration.days}")
    private int maxDurationDays;
}
