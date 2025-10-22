package com.example.NYA_reservation.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalTime;

@Getter
@Setter
public class RestaurantForm {
    private Integer id;
    private String name;
    private String telephone;
    private String address;
    private String area;
    private String genre;
    private String explanation;
    private LocalTime startBusiness;
    private LocalTime endBusiness;
    private Integer capacity;
    private Integer minAmount;
    private Integer maxAmount;
    private Timestamp createdDate;
    private Timestamp updatedDate;

}
