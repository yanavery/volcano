//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.exception;

//=================================================================================================
public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
