//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

//=================================================================================================
@Component
public class DateUtils {
    @Autowired
    private Configuration config;

    /**
     * Determines the current date.
     */
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Determines the startDate to be used when searching. Uses the provided date if given,
     * otherwise computes a default startDate using configuration information.
     */
    public LocalDate getSearchStartDateOrDefault(LocalDate date) {
        return date != null ? date : getCurrentDate().plusDays(config.getMinLeadTimeDays());
    }

    /**
     * Determines the endDate to be used when searching. Uses the provided date if given,
     * otherwise computes a default endDate using configuration information.
     */
    public LocalDate getSearchEndDateOrDefault(LocalDate date) {
        return date != null ? date : getCurrentDate().plusDays(config.getMaxLeadTimeDays());
    }

    /**
     * Determines whether the given date is within the allowed range, using configuration information.
     */
    public boolean isWithinDaysFuture(LocalDate date, int nbDays) {
        LocalDate minDate = getCurrentDate().minusDays(1);
        LocalDate maxDate = getCurrentDate().plusDays(nbDays + 1);
        return date.isAfter(minDate) && date.isBefore(maxDate);
    }
}
