package com.example.NYA_reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AreaReservationCountDto {

    private String area;
    private Long count;

    public AreaReservationCountDto(String area, Long count) {
        this.area = area;
        this.count = count;
    }

}
