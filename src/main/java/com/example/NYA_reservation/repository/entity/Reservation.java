package com.example.NYA_reservation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
public class Reservation {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDate reservationDate;

    @Column
    private LocalTime reservationTime;

    @Column
    private Integer headcount;

    @Column
    private Integer restaurantId;

    @Column
    private Integer userId;

    @Column(insertable = false, updatable = false)
    private Timestamp createdDate;

    @Column(insertable = false, updatable = true)
    private Timestamp updatedDate;
}
