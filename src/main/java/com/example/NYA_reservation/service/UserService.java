package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.UserForm;
import com.example.NYA_reservation.repository.UserRepository;
import com.example.NYA_reservation.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    // パスワードハッシュ化用
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // アカウント重複判定用
    public boolean isAccountExists(String account) {
        return userRepository.findByAccount(account).isPresent();
    }

    // ユーザー登録
    public boolean addUser(UserForm userForm) {

        if (isAccountExists(userForm.getAccount())) {
            return false;
        }

        User user = new User();
        user.setAccount(userForm.getAccount());
        user.setPassword(passwordEncoder.encode(userForm.getPassword())); // ハッシュ化
        user.setName(userForm.getName());

        userRepository.save(user);
        return true;
    }

    // ユーザー編集

}
