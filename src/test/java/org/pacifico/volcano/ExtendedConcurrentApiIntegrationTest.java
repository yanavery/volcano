//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.pacifico.volcano.beans.ReservationRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

//=================================================================================================
/*
 * Tests can run concurrently by annotating the test class with `@Execution(ExecutionMode.CONCURRENT)`,
 * combined with setting the `junit.jupiter.execution.parallel.enabled` property set to `true` in
 * the `junit-platform.properties` file. This feature is available as of jUnit 5.3.
 *
 * For more details, see https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution
 */
//=================================================================================================
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = "volcano.booking.max.lead.time.days=5000")
public class ExtendedConcurrentApiIntegrationTest extends MultiThreadedAbstractBaseTest {
	/** Number of dynamic tests to be launched concurrently by this test class. */
	private static final int NB_TESTS = 2000;
	/** Number of days for each of the test reservations to last. */
	private static final int NB_DAYS_PER_RESERVATION = 2;
	/** Number of days to offset start/end date for test reservations, avoids conflict with bootstrap data. */
	private static final int NB_DAYS_OFFSET = 30;

	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;

	//---------------------------------------------------------------------------------------------
	@TestFactory
	Collection<DynamicTest> getTestCasesToRun() {
		// Build batch of tests, with each checkIn/checkOut pair doubled, to test for concurrency.
		return Stream.concat(
					buildReservationRequests(NB_TESTS / 2).stream(), // 1st batch of tests.
					buildReservationRequests(NB_TESTS / 2).stream()) // 2nd batch of tests (with same dates).
				.map(reservation -> dynamicTest(String.format("Test for %s.", reservation.getCustomer().getEmail()),
						() -> testReservationCreation(reservation)))
				.collect(Collectors.toList());
	}

	//---------------------------------------------------------------------------------------------
	private void testReservationCreation(ReservationRequestBean request) {
		ResponseEntity<String> response = restTemplate.postForEntity(
				getReservationCreateEndPointUrl(port), request, String.class);

		dynamicAssert(request.getCheckIn(), request.getCheckOut(), response);
	}

	//---------------------------------------------------------------------------------------------
	private List<ReservationRequestBean> buildReservationRequests(int count) {
		List<ReservationRequestBean> requests = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			LocalDate checkIn = LocalDate.now().plusDays((i * NB_DAYS_PER_RESERVATION) + NB_DAYS_OFFSET);
			LocalDate checkOut = LocalDate.now().plusDays((i * NB_DAYS_PER_RESERVATION) + NB_DAYS_OFFSET + NB_DAYS_PER_RESERVATION);
			requests.add(TestUtils.mockReservation(checkIn, checkOut,
				TestUtils.mockCustomer(String.format("test%1$d@mail.com", i), String.format("Test%1$d User%1$d", i))));
		}
		return requests;
	}
}
