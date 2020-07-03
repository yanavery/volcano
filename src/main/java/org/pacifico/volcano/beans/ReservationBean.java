//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.beans;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

//=================================================================================================
@Getter
@Setter
public class ReservationBean {
    UUID uuid;
    CustomerBean customer;
    LocalDate checkIn;
    LocalDate checkOut;
}
