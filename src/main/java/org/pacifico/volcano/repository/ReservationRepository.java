//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.repository;

import org.pacifico.volcano.repository.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//=================================================================================================
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Retrieves the reservation with the given UUID value.
     */
    Optional<Reservation> findByUuid(UUID uuid);

    /**
     * Retrieves all reservations that fall within the provided start/end date range.
     */
    @Query(
        "SELECT r " +
        "FROM Reservation r " +
        "WHERE (r.checkIn < :end AND r.checkOut > :start) " +
        "ORDER BY r.checkIn")
    List<Reservation> findAllInRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
