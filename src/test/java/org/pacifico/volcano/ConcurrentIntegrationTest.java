//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.pacifico.volcano.beans.ReservationRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
@TestPropertySource(properties = "volcano.booking.max.lead.time.days=500")
public class ConcurrentIntegrationTest {
	/** Number of dynamic tests to be launched concurrently by this test class. */
	private static final int NB_TESTS = 100;
	/** Number of days for each of the test reservations to last. */
	private static final int NB_DAYS_PER_RESERVATION = 2;
	/** Number of days to offset start/end date for reservations, to avoid conflict with existing bootstrap data. */
	private static final int NB_DAYS_OFFSET = 30;

	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;

	//---------------------------------------------------------------------------------------------
	@TestFactory
	Collection<DynamicTest> getTestCasesToRun() {
		return buildTestMocks(NB_TESTS)
				.stream()
				.map(reservation -> dynamicTest(String.format("Test for %s.", reservation.getCustomer().getEmail()),
						() -> testReservationCreation(reservation)))
				.collect(Collectors.toList());
	}

	//---------------------------------------------------------------------------------------------
	private void testReservationCreation(ReservationRequestBean request) {
		ResponseEntity<String> response = restTemplate.postForEntity(
				getReservationCreateEndPointUrl(), request, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertThat(response.getBody(), containsString(request.getCustomer().getEmail()));
	}

	//---------------------------------------------------------------------------------------------
	private String getReservationCreateEndPointUrl() {
		return "http://localhost:" + port + "/v1/reservations";
	}

	//---------------------------------------------------------------------------------------------
	private List<ReservationRequestBean> buildTestMocks(int count) {
		List<ReservationRequestBean> mocks = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			LocalDate checkIn = LocalDate.now().plusDays((i * NB_DAYS_PER_RESERVATION) + NB_DAYS_OFFSET);
			LocalDate checkOut = LocalDate.now().plusDays((i * NB_DAYS_PER_RESERVATION) + NB_DAYS_OFFSET + NB_DAYS_PER_RESERVATION);
			mocks.add(TestUtils.mockReservation(checkIn, checkOut,
				TestUtils.mockCustomer(String.format("test%1$d@mail.com", i), String.format("Test%1$d User%1$d", i))));
		}
		return mocks;
	}
}
