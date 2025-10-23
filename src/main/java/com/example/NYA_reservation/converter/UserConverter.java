package com.example.NYA_reservation.converter;

import com.example.NYA_reservation.controller.form.UserForm;
import com.example.NYA_reservation.repository.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    // List<User> → List<UserForm>
    public List<UserForm> toUserFormList(List<User> userList) {
        List<UserForm> users = new ArrayList<>();

        for (User result : userList) {
            UserForm user = new UserForm();
            user.setId(result.getId());
            user.setAccount(result.getAccount());
            user.setPassword(result.getPassword());
            user.setName(result.getName());
            user.setAuthority(result.getAuthority());
            user.setStopped(result.isStopped());
            user.setCreatedDate(result.getCreatedDate());
            user.setUpdatedDate(result.getUpdatedDate());
            users.add(user);
        }
        return users;
    }

}
