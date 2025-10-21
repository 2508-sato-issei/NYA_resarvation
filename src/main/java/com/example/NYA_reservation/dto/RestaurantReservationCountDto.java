package com.example.NYA_reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantReservationCountDto {

    private Integer id;
    private String name;
    private Long count;

    public RestaurantReservationCountDto(Integer id, String name, Long count) {
        this.id = id;
        this.name = name;
        this.count = count;

    }

}
