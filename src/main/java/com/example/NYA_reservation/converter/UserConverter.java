package com.example.NYA_reservation.converter;

import com.example.NYA_reservation.controller.form.UserForm;
import com.example.NYA_reservation.repository.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    // User → UserForm
    public UserForm toForm(User user) {
        UserForm userForm = new UserForm();
        userForm.setId(user.getId());
        userForm.setAccount(user.getAccount());
        userForm.setPassword(user.getPassword());
        userForm.setName(user.getName());
        userForm.setAuthority(user.getAuthority());
        userForm.setStopped(user.isStopped());
        userForm.setCreatedDate(user.getCreatedDate());
        userForm.setUpdatedDate(user.getUpdatedDate());
        return userForm;
    }

}
