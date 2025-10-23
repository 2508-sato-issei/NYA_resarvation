package com.example.NYA_reservation.controller.form;

import com.example.NYA_reservation.repository.entity.Restaurant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.NYA_reservation.validation.ErrorMessage.*;

@Getter
@Setter
public class ReservationForm {
    private Integer id;

    @NotNull(message = E0004)
    private LocalDate reservationDate;

    @NotNull(message = E0005)
    private LocalTime reservationTime;

    @NotNull(message = E0006)
    private Integer headcount;

    private Integer restaurantId;
    private Restaurant restaurant;
    private Integer userId;
    private Timestamp createdDate;
    private Timestamp updatedDate;

}
