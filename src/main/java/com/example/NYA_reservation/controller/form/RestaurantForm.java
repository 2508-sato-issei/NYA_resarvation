package com.example.NYA_reservation.controller.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalTime;

import static com.example.NYA_reservation.validation.ErrorMessage.*;

@Getter
@Setter
public class RestaurantForm {
    private Integer id;

    @NotEmpty(message = E0018)
    @Pattern(regexp = "^(?:$|.*[^\\s　].*)$", message = E0018)
    @Size(max=50, message = E0029)
    private String name;

    @NotEmpty(message = E0019)
    @Pattern(regexp = "$|[0-9]{2}-[0-9]{4}-[0-9]{4}|[0-9]{3}-[0-9]{3}-[0-9]{4}|[0-9]{3}-[0-9]{4}-[0-9]{4}", message = E0031)
    private String telephone;

    @NotEmpty(message = E0020)
    @Pattern(regexp = "^(?:$|.*[^\\s　].*)$", message = E0020)
    private String address;

    @NotEmpty(message = E0021)
    private String area;

    @NotEmpty(message = E0022)
    private String genre;

    @NotNull(message = E0023)
    @Pattern(regexp = "(?s)^(?![\\s　]*$).+", message = E0023)
    @Size(max=500, message = E0030)
    private String explanation;

    @NotNull(message = E0024)
    private LocalTime startBusiness;

    @NotNull(message = E0025)
    private LocalTime endBusiness;

    @NotNull(message = E0026)
    private Integer capacity;

    @NotNull(message = E0027)
    private Integer minAmount;

    @NotNull(message = E0028)
    private Integer maxAmount;

    private MultipartFile mainImage;

    private Timestamp createdDate;
    private Timestamp updatedDate;

}
