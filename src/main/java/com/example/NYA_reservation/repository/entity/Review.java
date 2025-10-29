package com.example.NYA_reservation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Column
    private Integer rating;

    @Column
    private String comment;

    @Column(insertable = false, updatable = false)
    private Timestamp createdDate;

    @Column(insertable = false)
    private Timestamp updatedDate;

}
