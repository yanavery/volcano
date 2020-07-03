//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//=================================================================================================
public abstract class MultiThreadedAbstractBaseTest {
    /** Thread safe set collection to hold already tested checkIn/checkOut date pairs. */
    private final Set<Pair<LocalDate, LocalDate>> executedTestCases = new CopyOnWriteArraySet<>();

    //---------------------------------------------------------------------------------------------
    protected void dynamicAssert(LocalDate checkIn, LocalDate checkOut, ResponseEntity<String> response) {
        Pair<LocalDate, LocalDate> dateRange = Pair.of(checkIn, checkOut);
        if (executedTestCases.contains(dateRange)) {
            // If this is a checkIn/checkOut range that was already booked, we're expecting failure.
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertThat(response.getBody(), containsString("not available for reservation"));
        } else {
            // If this is a checkIn/checkOut range that wasn't already booked, we're expecting success.
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            // Add this checkIn/checkOut range to the already booked collection.
            executedTestCases.add(dateRange);
        }
    }

    //---------------------------------------------------------------------------------------------
    protected String getReservationCreateEndPointUrl(int port) {
        return "http://localhost:" + port + "/v1/reservations";
    }
}
