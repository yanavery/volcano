//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.api;

import org.pacifico.volcano.beans.ReservationBean;
import org.pacifico.volcano.beans.ReservationRequestBean;
import org.pacifico.volcano.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//=================================================================================================
@RestController
@RequestMapping("/v1/reservations")
public class ReservationApi {
    @Autowired
    private ReservationService reservationService;

    /**
     * GET API end-point, used to retrieve an existing reservation.
     *
     * Sample cURL usage:
     * ------------------
     * curl http://localhost:8080/v1/reservations/{uuid}
     */
    @GetMapping("/{uuid}")
    public HttpEntity<ReservationBean> read(@PathVariable("uuid") UUID uuid) {
        ReservationBean reservationBean = reservationService.read(uuid);
        return new ResponseEntity<>(reservationBean, HttpStatus.OK);
    }

    /**
     * POST API end-point, used to create a new reservation.
     *
     * Sample cURL usage:
     * ------------------
     * curl -X POST -H 'Content-Type: application/json' -d \
     * '{
     *    "customer": {
     *       "email": "yan_avery@yopmail.com",
     *       "name": "Yan Avery"
     *    },
     *    "checkIn": "2020-07-12",
     *    "checkOut": "2020-07-14"
     * }' \
     * http://localhost:8080/v1/reservations
     */
    @PostMapping
    public HttpEntity<ReservationBean> create(@RequestBody ReservationRequestBean request) {
        ReservationBean reservationBean = reservationService.create(request);
        return new ResponseEntity<>(reservationBean, HttpStatus.CREATED);
    }

    /**
     * PUT API end-point, used to update an existing reservation.
     *
     * Sample cURL usage:
     * ------------------
     * curl -X PUT -H 'Content-Type: application/json' -d \
     * '{
     *    "checkIn": "2020-07-14",
     *    "checkOut": "2020-07-16"
     * }' \
     * http://localhost:8080/v1/reservations/{uuid}
     */
    @PutMapping("/{uuid}")
    public HttpEntity<ReservationBean> update(@PathVariable("uuid") UUID uuid,
                                              @RequestBody ReservationRequestBean request) {
        ReservationBean reservationBean = reservationService.update(uuid, request);
        return new ResponseEntity<>(reservationBean, HttpStatus.OK);
    }

    /**
     * DELETE API end-point, used to cancel (physically delete) an existing reservation.
     *
     * Sample cURL usage:
     * ------------------
     * curl -X DELETE http://localhost:8080/v1/reservations/{uuid}
     */
    //---------------------------------------------------------------------------------------------
    @DeleteMapping("/{uuid}")
    public HttpEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        reservationService.delete(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
