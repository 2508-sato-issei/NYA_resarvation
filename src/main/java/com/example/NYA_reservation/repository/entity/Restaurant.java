package com.example.NYA_reservation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalTime;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
public class Restaurant {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String telephone;

    @Column
    private String address;

    @Column
    private String area;

    @Column
    private String genre;

    @Column
    private String explanation;

    @Column(name = "start_business", nullable = false)
    private LocalTime startBusiness;

    @Column(name = "end_business", nullable = false)
    private LocalTime endBusiness;

    @Column
    private Integer capacity;

    @Column(name = "min_amount", nullable = false)
    private Integer minAmount;

    @Column(name = "max_amount", nullable = false)
    private Integer maxAmount;

    @Column(insertable = false, updatable = false)
    private Timestamp createdDate;

    @Column(insertable = false, updatable = true)
    private Timestamp updatedDate;
}
