//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.VisibleForTesting;
import org.pacifico.volcano.beans.AvailabilityBean;
import org.pacifico.volcano.beans.AvailabilityCriteriaBean;
import org.pacifico.volcano.repository.ReservationRepository;
import org.pacifico.volcano.repository.model.Reservation;
import org.pacifico.volcano.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//=================================================================================================
@Service
@Slf4j
public class AvailabilityService {
    @Autowired
    private ValidationUtils validationUtils;
    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Returns all availabilities within the criteria provided date range.
     */
    public List<AvailabilityBean> getAvailabilities(AvailabilityCriteriaBean criteria) {
        validationUtils.validateDatesForSearching(criteria.getStartDate(), criteria.getEndDate());

        // Determine all potential availabilities within the provided date range.
        Set<LocalDate> availableDates = getAllDatesFromRange(
                criteria.getStartDate(), criteria.getEndDate());
        log.debug("Available dates: {}.", availableDates);

        // Retrieve existing reservations within the provided date range.
        List<Reservation> reservationsInRange = reservationRepository
                .findAllInRange(criteria.getStartDate(), criteria.getEndDate());
        Set<LocalDate> reservedDates = getAllDatesFromReservations(reservationsInRange);
        log.debug("Reserved dates: {}.", reservedDates);

        // Remove any availability for which a reservation already exists.
        availableDates.removeAll(reservedDates);
        log.debug("Remaining available dates: {}.", availableDates);

        return availableDates
                .stream()
                .map(AvailabilityBean::new)
                .collect(Collectors.toList());
    }

    /**
     * Returns true if the given start/end date range is available for reservation, otherwise false.
     */
    public boolean isAvailableForReservation(LocalDate checkIn, LocalDate checkOut) {
        return isAvailableForReservation(checkIn, checkOut, Collections.emptySet());
    }

    /**
     * Returns true if the given start/end date range is available for reservation, otherwise false.
     * Allows caller to provide a list of reservation UUIDs to exclude from processing, which can
     * be useful when updating an existing reservation.
     */
    public boolean isAvailableForReservation(LocalDate checkIn, LocalDate checkOut, Set<UUID> exclusions) {
        List<Reservation> reservationsInRange =
                reservationRepository.findAllInRange(checkIn, checkOut)
                        .stream()
                        .filter(reservation -> !exclusions.contains(reservation.getUuid()))
                        .collect(Collectors.toList());

        return reservationsInRange.size() == 0;
    }

    /**
     * Determines all dates that fall within the provided start/end date range.
     */
    @VisibleForTesting
    Set<LocalDate> getAllDatesFromRange(LocalDate start, LocalDate end) {
        Set<LocalDate> datesInRange = new TreeSet<>();
        for (LocalDate d = start; d.isBefore(end) || d.isEqual(end); d = d.plusDays(1)) {
            datesInRange.add(d);
        }
        return datesInRange;
    }

    /**
     * Determines all dates that fall within the provided reservations' start/end date ranges.
     */
    @VisibleForTesting
    Set<LocalDate> getAllDatesFromReservations(List<Reservation> reservations) {
        return reservations
                .stream()
                .map(reservation -> getAllDatesFromRange(reservation.getCheckIn(), reservation.getCheckOut()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
