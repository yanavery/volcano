//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.utils;

import org.modelmapper.ModelMapper;
import org.pacifico.volcano.beans.AppExceptionBean;
import org.pacifico.volcano.beans.ReservationBean;
import org.pacifico.volcano.repository.model.Reservation;
import org.springframework.stereotype.Component;

//=================================================================================================
@Component
public class Mapper extends ModelMapper {

    /**
     * Maps the given Exception to an AppException.
     */
    public AppExceptionBean toBean(Exception e) {
        return map(e, AppExceptionBean.class);
    }

    /**
     * Maps the given Reservation entity to a ReservationBean.
     */
    public ReservationBean toBean(Reservation reservation) {
        return map(reservation, ReservationBean.class);
    }
}
