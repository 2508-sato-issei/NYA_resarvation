package com.example.NYA_reservation.controller.form;

import com.example.NYA_reservation.repository.entity.Restaurant;
import com.example.NYA_reservation.repository.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ReviewForm {

    private Long id;

    private Restaurant restaurant;

    private User user;

    private String title;

    private Integer rating;

    private String comment;

    private Timestamp createdDate;

    private Timestamp updatedDate;

}
