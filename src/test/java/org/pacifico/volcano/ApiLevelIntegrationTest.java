//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano;

import org.junit.jupiter.api.Test;
import org.pacifico.volcano.beans.ReservationRequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//=================================================================================================
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiLevelIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;

	//---------------------------------------------------------------------------------------------
	@Test
	void testSequentialReservationCreation_1_Success() {
		// 1st reservation - should go thru.
		ReservationRequestBean request = TestUtils.mockReservation(
				LocalDate.now().plusDays(20),
				LocalDate.now().plusDays(22),
				TestUtils.mockCustomer("test_user1@mail.com", "Test1 User1"));
		ResponseEntity<String> response =
				restTemplate.postForEntity(getReservationCreateEndPointUrl(), request, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertThat(response.getBody(), containsString("test_user1@mail.com"));
	}

	//---------------------------------------------------------------------------------------------
	@Test
	void testSequentialReservationCreation_2_Failure() {
		// 2nd reservation - should fail, since it has the same date range as the previous one.
		ReservationRequestBean request = TestUtils.mockReservation(
				LocalDate.now().plusDays(20),
				LocalDate.now().plusDays(22),
				TestUtils.mockCustomer("test_user2@mail.com", "Test2 User2"));
		ResponseEntity<String> response =
				restTemplate.postForEntity(getReservationCreateEndPointUrl(), request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertThat(response.getBody(), containsString("not available for reservation"));
	}

	//---------------------------------------------------------------------------------------------
	@Test
	void testSequentialReservationCreation_3_Success() {
		// 3rd reservation - should pass, since it has different date range than the previous one.
		ReservationRequestBean request = TestUtils.mockReservation(
				LocalDate.now().plusDays(23),
				LocalDate.now().plusDays(24),
				TestUtils.mockCustomer("test_user3@mail.com", "Test3 User3"));
		ResponseEntity<String> response =
				restTemplate.postForEntity(getReservationCreateEndPointUrl(), request, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertThat(response.getBody(), containsString("test_user3@mail.com"));
	}

	//---------------------------------------------------------------------------------------------
	private String getReservationCreateEndPointUrl() {
		return "http://localhost:" + port + "/v1/reservations";
	}
}
