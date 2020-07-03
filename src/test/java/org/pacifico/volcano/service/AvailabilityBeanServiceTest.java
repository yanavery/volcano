//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pacifico.volcano.repository.model.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

//=================================================================================================
@ExtendWith(MockitoExtension.class)
public class AvailabilityBeanServiceTest {
    @InjectMocks
    private AvailabilityService availabilityService;

    //---------------------------------------------------------------------------------------------
    @Test
    void testGetAllDatesFromRange() {
        LocalDate startDate = LocalDate.parse("2020-07-15", DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse("2020-07-30", DateTimeFormatter.ISO_DATE);

        Set<LocalDate> actualDates = availabilityService.getAllDatesFromRange(startDate, endDate);
        Set<LocalDate> expectedDates = ExpectedBuilder.getAvailabilities(startDate, endDate);

        assertEquals(expectedDates, actualDates);
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testGetAllDatesFromReservations() {
        List<Reservation> reservations = MockBuilder.getReservations();

        Set<LocalDate> actualDates = availabilityService.getAllDatesFromReservations(reservations);
        Set<LocalDate> expectedDates = ExpectedBuilder.getAvailabilities(reservations);

        assertEquals(expectedDates, actualDates);
    }

    //=============================================================================================
    private static class ExpectedBuilder {

        //-----------------------------------------------------------------------------------------
        private static Set<LocalDate> getAvailabilities(LocalDate start, LocalDate end) {
            Set<LocalDate> expectedDates = new TreeSet<>();
            for (LocalDate d = start; d.isBefore(end) || d.isEqual(end); d = d.plusDays(1)) {
                expectedDates.add(d);
            }
            return expectedDates;
        }

        //-----------------------------------------------------------------------------------------
        private static Set<LocalDate> getAvailabilities(List<Reservation> reservations) {
            Set<LocalDate> expectedDates = new TreeSet<>();
            for (Reservation reservation : reservations) {
                expectedDates.addAll(getAvailabilities(reservation.getCheckIn(), reservation.getCheckOut()));
            }
            return expectedDates;
        }
    }

    //=============================================================================================
    private static class MockBuilder {

        //-----------------------------------------------------------------------------------------
        private static List<Reservation> getReservations() {
            List<Reservation> reservations = new ArrayList<>();
            LocalDate now = LocalDate.now();

            reservations.add(getReservation(now.plusDays(1), now.plusDays(3)));
            reservations.add(getReservation(now.plusDays(3), now.plusDays(5)));
            reservations.add(getReservation(now.plusDays(10), now.plusDays(12)));
            reservations.add(getReservation(now.plusDays(15), now.plusDays(16)));

            return reservations;
        }

        //-----------------------------------------------------------------------------------------
        private static Reservation getReservation(LocalDate checkIn, LocalDate checkOut) {
            Reservation reservation = new Reservation();
            reservation.setCheckIn(checkIn);
            reservation.setCheckOut(checkOut);

            return reservation;
        }
    }
}
