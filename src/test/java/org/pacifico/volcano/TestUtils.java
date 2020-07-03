//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano;

import org.pacifico.volcano.beans.CustomerBean;
import org.pacifico.volcano.beans.ReservationRequestBean;

import java.time.LocalDate;

//=================================================================================================
public class TestUtils {

    //---------------------------------------------------------------------------------------------
    public static CustomerBean mockCustomer(String email, String name) {
        CustomerBean customer = new CustomerBean();
        customer.setEmail(email);
        customer.setName(name);
        return customer;
    }

    //---------------------------------------------------------------------------------------------
    public static ReservationRequestBean mockReservation(LocalDate checkIn, LocalDate checkOut, CustomerBean customer) {
        ReservationRequestBean reservationRequest = new ReservationRequestBean();
        reservationRequest.setCheckIn(checkIn);
        reservationRequest.setCheckOut(checkOut);
        reservationRequest.setCustomer(customer);
        return reservationRequest;
    }
}
