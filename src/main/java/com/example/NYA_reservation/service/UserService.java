package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.error.RecordNotFoundException;
import com.example.NYA_reservation.controller.form.UserForm;
import com.example.NYA_reservation.converter.UserConverter;
import com.example.NYA_reservation.repository.UserRepository;
import com.example.NYA_reservation.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.NYA_reservation.validation.ErrorMessage.E0011;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserConverter userConverter;

    // パスワードハッシュ化用
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // アカウント重複判定用
    public boolean isAccountExists(String account, Integer selfId) {
        Optional<User> user = userRepository.findByAccount(account);

        // アカウント未使用ならOK
        if (user.isEmpty()) {
            return false;
        }

        // 新規登録 → 誰か使ってたらNG
        if (selfId == null) {
            return true;
        }

        // 更新時 → 自分以外が使ってたらNG
        return !user.get().getId().equals(selfId);
    }

    // IDからユーザー取得
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0011));
    }

    //総会員数取得
    public Long countUsers(){
        return userRepository.count();
    }

    // User → UserForm 変換
    public UserForm convertToForm(User user) {
        return userConverter.toForm(user);
    }

    // ユーザー登録
    public boolean addUser(UserForm userForm) {

        if (isAccountExists(userForm.getAccount(), null)) {
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
    public boolean updateUser(UserForm userForm) {

        if (isAccountExists(userForm.getAccount(), userForm.getId())) {
            return false;
        }

        User updateUser = findById(userForm.getId());

        if (userForm.getPassword() != null && !userForm.getPassword().isBlank()) {
            updateUser.setPassword(passwordEncoder.encode(userForm.getPassword()));
        }
        updateUser.setAccount(userForm.getAccount());
        updateUser.setName(userForm.getName());
        updateUser.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

        userRepository.save(updateUser);
        return true;
    }

    //ユーザー情報全件取得
//    public List<UserForm> findAllUser() {
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
//        List<User> results = userRepository.findAll(sort);
//        return userConverter.toUserFormList(results);
//    }

    public Page<UserForm> pageUser(Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<User> page = userRepository.findAll(sortedPageable);
        return page.map(userConverter::toForm);
    }

    //ユーザー停止・有効切り替え
    public void changeIsStopped(Integer id, boolean isStopped) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        userRepository.changeIsStopped(id, isStopped, ts);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("指定されたユーザーが存在しません");
        }
        userRepository.deleteById(id);
    }
}
