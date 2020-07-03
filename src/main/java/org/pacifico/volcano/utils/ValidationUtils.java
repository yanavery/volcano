//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.utils;

import org.assertj.core.util.VisibleForTesting;
import org.pacifico.volcano.beans.CustomerBean;
import org.pacifico.volcano.exception.InvalidParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

//=================================================================================================
@Component
public class ValidationUtils {
    @Autowired
    private Configuration config;
    @Autowired
    private DateUtils dateUtils;

    /**
     * Validates the provided start/end date range is valid for
     * searching purposes, throws exception otherwise.
     */
    public void validateDatesForSearching(LocalDate startDate, LocalDate endDate) {
        validateFutureDated(startDate);
        validateStartDateBeforeEndDate(startDate, endDate);
        validateDateProximity(startDate, endDate);
    }

    /**
     * Validates the provided start/end date range is valid for
     * persisting purposes, throws exception otherwise.
     */
    public void validateDatesForSaving(LocalDate startDate, LocalDate endDate) {
        validateFutureDated(startDate);
        validateStartDateBeforeEndDate(startDate, endDate);
        validateDateProximity(startDate, endDate);
        validateStartEndDateMaxSpread(startDate, endDate);
        validateStartEndDateMinSpread(startDate, endDate);
    }

    /**
     * Validates the provided customer is valid, throws exception otherwise.
     */
    public void validateCustomer(CustomerBean customer) {
        validateCustomerPresence(customer);
        validateCustomerEmail(customer);
        validateCustomerName(customer);
    }

    /**
     * Validates the provided start date is in the future, throws exception otherwise.
     */
    @VisibleForTesting
    void validateFutureDated(LocalDate startDate) {
        if (startDate.isBefore(dateUtils.getCurrentDate())) {
            throw new InvalidParameterException(
                    String.format("Start date (%1$tY-%1$tm-%1$td) must be in the future.", startDate));
        }
    }

    /**
     * Validates the provided start date is before the provided end date, throws exception otherwise.
     */
    @VisibleForTesting
    void validateStartDateBeforeEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidParameterException(
                    String.format("Start date (%1$tY-%1$tm-%1$td) must be before end date (%2$tY-%2$tm-%2$td).",
                            startDate, endDate));
        }
    }

    /**
     * Validates the provided start/end date range isn't too wide, throws exception otherwise.
     */
    @VisibleForTesting
    void validateStartEndDateMaxSpread(LocalDate startDate, LocalDate endDate) {
        int maxDuration = config.getMaxDurationDays();
        if (DAYS.between(startDate, endDate) > maxDuration) {
            throw new InvalidParameterException(
                    String.format("Invalid date range. There's a maximum of %d day(s) per reservation.",
                            maxDuration));
        }
    }

    /**
     * Validates the provided start/end date range isn't too narrow, throws exception otherwise.
     */
    @VisibleForTesting
    void validateStartEndDateMinSpread(LocalDate startDate, LocalDate endDate) {
        int minDuration = config.getMinDurationDays();
        if (DAYS.between(startDate, endDate) < minDuration) {
            throw new InvalidParameterException(
                    String.format("Invalid date rage. There's a minimum of %d day(s) per reservation.",
                            minDuration));
        }
    }

    /**
     * Validates the provided start/end date range is not too far in the future, throws exception otherwise.
     */
    @VisibleForTesting
    void validateDateProximity(LocalDate startDate, LocalDate endDate) {
        int maxStartDays = config.getMaxLeadTimeDays();
        int maxEndDays = config.getMaxLeadTimeDays() + config.getMaxDurationDays();
        if (!dateUtils.isWithinDaysFuture(startDate, maxStartDays) ||
                !dateUtils.isWithinDaysFuture(endDate, maxEndDays)) {
            throw new InvalidParameterException("Can't search for availabilities too far away in the future.");
        }
    }

    /**
     * Validates the customer is provided, throws exception otherwise.
     */
    @VisibleForTesting
    void validateCustomerPresence(CustomerBean customer) {
        if (customer == null) {
            throw new InvalidParameterException("Customer information is missing from reservation request.");
        }
    }

    /**
     * Validates the customer email is provided, throws exception otherwise.
     */
    @VisibleForTesting
    void validateCustomerEmail(CustomerBean customer) {
        if (StringUtils.isEmpty(customer.getEmail())) {
            throw new InvalidParameterException("Customer email is missing from reservation request.");
        }
    }

    /**
     * Validates the customer name is provided, throws exception otherwise.
     */
    @VisibleForTesting
    void validateCustomerName(CustomerBean customer) {
        if (StringUtils.isEmpty(customer.getName())) {
            throw new InvalidParameterException("Customer name is missing from reservation request.");
        }
    }
}
