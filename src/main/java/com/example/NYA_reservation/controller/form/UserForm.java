package com.example.NYA_reservation.controller.form;

import com.example.NYA_reservation.validation.CreateGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

import static com.example.NYA_reservation.validation.ErrorMessage.*;

@Getter
@Setter
public class UserForm {

    private Integer id;

    @NotEmpty(message = E0001)
    @Size(max = 10, message = E0013)
    private String account;

    @NotEmpty(message = E0002, groups = {CreateGroup.class})
    @Pattern(regexp = "^(?:$|[\\x21-\\x7E]{6,20})$", message = E0014)
    private String password;

    @NotEmpty(message = E0035, groups = {CreateGroup.class})
    private String confirmPassword; // パスワード（確認用）

    @NotEmpty(message = E0012)
    @Size(max = 10, message = E0015)
    private String name;

    private Integer authority;
    private boolean isStopped;
    private Timestamp createdDate;
    private Timestamp updatedDate;

}
