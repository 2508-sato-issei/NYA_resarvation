package com.example.NYA_reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreReservationCountDto {

    private String genre;
    private Long count;

    public GenreReservationCountDto(String genre, Long count) {
        this.genre = genre;
        this.count = count;
    }

}
