//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.beans;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

//=================================================================================================
@Getter
@Setter
public class ReservationRequestBean {
    CustomerBean customer;
    LocalDate checkIn;
    LocalDate checkOut;
}
