package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.UserForm;
import com.example.NYA_reservation.repository.UserRepository;
import com.example.NYA_reservation.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    //ユーザー情報全件取得
    public List<UserForm> findAllUser(){
        List<User> results = userRepository.findAll();
        return setUserForm(results);
    }

    //ユーザー停止・有効切り替え
    public void changeIsStopped(Integer id, boolean isStopped){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        userRepository.changeIsStopped(id, isStopped, ts);
    }


    private List<UserForm> setUserForm(List<User> results){
        List<UserForm> users = new ArrayList<>();

        for(User result : results){
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
