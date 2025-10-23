package com.example.NYA_reservation.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class RegularHolidayForm {
    private Integer id;
    private Integer restaurantId;
    private Integer regularHoliday;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
