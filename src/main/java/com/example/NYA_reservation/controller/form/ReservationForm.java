package com.example.NYA_reservation.controller.form;

import com.example.NYA_reservation.repository.entity.Restaurant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.NYA_reservation.validation.ErrorMessage.*;

@Getter
@Setter
public class ReservationForm {
    private Integer id;

    @NotNull(message = E0004)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;

    @NotNull(message = E0005)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime reservationTime;

    @NotNull(message = E0006)
    private Integer headcount;

    private Integer restaurantId;
    private Restaurant restaurant;
    private Integer userId;
    private Timestamp createdDate;
    private Timestamp updatedDate;

}
