package com.example.NYA_reservation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String account;

    @Column
    private String password;

    @Column
    private String name;

    @Column(insertable = false, updatable = false)
    private Integer authority;

    @Column(insertable = false, updatable = false)
    private boolean isStopped;

    @Column(insertable = false, updatable = false)
    private Timestamp createdDate;

    @Column(insertable = false, updatable = true)
    private Timestamp updatedDate;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}
