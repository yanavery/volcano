//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.service;

import org.assertj.core.util.Sets;
import org.pacifico.volcano.beans.ReservationBean;
import org.pacifico.volcano.beans.ReservationRequestBean;
import org.pacifico.volcano.exception.InvalidParameterException;
import org.pacifico.volcano.exception.ReservationNotFoundException;
import org.pacifico.volcano.repository.ReservationRepository;
import org.pacifico.volcano.repository.model.Reservation;
import org.pacifico.volcano.utils.Mapper;
import org.pacifico.volcano.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

//=================================================================================================
@Service
public class ReservationService {
    @Autowired
    private ValidationUtils validationUtils;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private Mapper mapper;

    /**
     * Retrieves a reservation using the provided reservation uuid.
     */
    public ReservationBean read(UUID reservationUuid) {
        Reservation entity = getReservation(reservationUuid);
        return mapper.toBean(entity);
    }

    /**
     * Creates a reservation using the provided reservation request data.
     */
    @Transactional
    public ReservationBean create(ReservationRequestBean request) {
        performValidationsForCreate(request);

        Reservation entity = new Reservation();
        entity.setUuid(UUID.randomUUID());
        entity.setCheckIn(request.getCheckIn());
        entity.setCheckOut(request.getCheckOut());
        entity.setCustomer(customerService.findOrCreate(request.getCustomer()));
        entity = reservationRepository.save(entity);

        return mapper.toBean(entity);
    }

    /**
     * Updates the reservation information, using the provided reservation uuid and reservation request data.
     */
    @Transactional
    public ReservationBean update(UUID reservationUuid, ReservationRequestBean request) {
        performValidationsForUpdate(request, reservationUuid);

        Reservation entity = getReservation(reservationUuid);
        entity.setCheckIn(request.getCheckIn());
        entity.setCheckOut(request.getCheckOut());
        entity = reservationRepository.save(entity);

        return mapper.toBean(entity);
    }

    /**
     * Deletes the reservation, using the provided reservation uuid.
     */
    @Transactional
    public void delete(UUID reservationUuid) {
        Reservation entity = getReservation(reservationUuid);
        reservationRepository.delete(entity);
    }

    /**
     * Retrieves the reservation with the given uuid, if not found throws an exception.
     */
    private Reservation getReservation(UUID reservationUuid) {
        Optional<Reservation> reservation = reservationRepository.findByUuid(reservationUuid);
        return reservation.orElseThrow(() -> new ReservationNotFoundException(
                String.format("The reservation ('%s') was not found.", reservationUuid)));
    }

    /**
     * Performs all necessary CREATE validations on the provided request.
     */
    private void performValidationsForCreate(ReservationRequestBean request) {
        validationUtils.validateDatesForSaving(request.getCheckIn(), request.getCheckOut());
        validationUtils.validateCustomer(request.getCustomer());

        // Determine if the requested checkIn/checkOut dates are available for reservation.
        if (!availabilityService.isAvailableForReservation(request.getCheckIn(), request.getCheckOut())) {
            throw new InvalidParameterException("The provided date range is not available for reservation.");
        }
    }

    /**
     * Performs all necessary UPDATE validations on the provided request.
     */
    private void performValidationsForUpdate(ReservationRequestBean request, UUID reservationUuid) {
        validationUtils.validateDatesForSaving(request.getCheckIn(), request.getCheckOut());

        // Determine if the requested checkIn/checkOut dates are available for reservation,
        // making sure to exclude the current reservation from lookup.
        Set<UUID> exclusions = Sets.newTreeSet(reservationUuid);
        if (!availabilityService.isAvailableForReservation(request.getCheckIn(), request.getCheckOut(), exclusions)) {
            throw new InvalidParameterException("The provided date range is not available for reservation.");
        }
    }
}
