//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.pacifico.volcano.beans.ReservationRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

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
public class SimpleConcurrentApiIntegrationTest extends MultiThreadedAbstractBaseTest {
	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;

	//---------------------------------------------------------------------------------------------
	@Test
	void testReservationCreation_1() {
		LocalDate checkIn = LocalDate.now().plusDays(20);
		LocalDate checkOut = LocalDate.now().plusDays(22);

		ReservationRequestBean request = TestUtils.mockReservation(
				checkIn, checkOut, TestUtils.mockCustomer("test_user1@mail.com", "Test1 User1"));
		ResponseEntity<String> response =
				restTemplate.postForEntity(getReservationCreateEndPointUrl(port), request, String.class);

		dynamicAssert(checkIn, checkOut, response);
	}

	//---------------------------------------------------------------------------------------------
	@Test
	void testReservationCreation_2() {
		LocalDate checkIn = LocalDate.now().plusDays(20);
		LocalDate checkOut = LocalDate.now().plusDays(22);

		ReservationRequestBean request = TestUtils.mockReservation(
				checkIn, checkOut, TestUtils.mockCustomer("test_user2@mail.com", "Test2 User2"));
		ResponseEntity<String> response =
				restTemplate.postForEntity(getReservationCreateEndPointUrl(port), request, String.class);

		dynamicAssert(checkIn, checkOut, response);
	}

	//---------------------------------------------------------------------------------------------
	@Test
	void testReservationCreation_3() {
		LocalDate checkIn = LocalDate.now().plusDays(23);
		LocalDate checkOut = LocalDate.now().plusDays(24);

		ReservationRequestBean request = TestUtils.mockReservation(
				checkIn, checkOut, TestUtils.mockCustomer("test_user3@mail.com", "Test3 User3"));
		ResponseEntity<String> response =
				restTemplate.postForEntity(getReservationCreateEndPointUrl(port), request, String.class);

		dynamicAssert(checkIn, checkOut, response);
	}
}
