package com.example.NYA_reservation.repository;

import com.example.NYA_reservation.repository.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r JOIN FETCH r.restaurant " +
            "WHERE r.userId = :userId " +
            "ORDER BY r.reservationDate ASC, r.reservationTime ASC")
    List<Reservation> findByUserIdOrderByReservationDateAsc(@Param("userId") Integer userId);
}
