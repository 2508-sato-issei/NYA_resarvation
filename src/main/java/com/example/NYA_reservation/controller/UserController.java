package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.UserForm;
import com.example.NYA_reservation.repository.entity.User;
import com.example.NYA_reservation.service.UserService;
import com.example.NYA_reservation.validation.CreateGroup;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.NYA_reservation.validation.ErrorMessage.E0016;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    // 新規会員登録画面表示
    @GetMapping("/new")
    public String showSignupForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "signup";
    }

    // 新規会員登録処理
    @PostMapping("/add")
    public String addUser(@ModelAttribute("userForm") @Validated({Default.class, CreateGroup.class}) UserForm userForm,
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("validationErrors", result);
            model.addAttribute("userForm", userForm);
            return "signup";
        }

        boolean success = userService.addUser(userForm);

        if (!success) {
            model.addAttribute("accountError", E0016);
            model.addAttribute("userForm", userForm);
            return "signup";
        }

        return "redirect:/login";
    }

    // 会員情報編集画面表示
    @GetMapping("/edit/{id}")
    public String showUserEditForm(@PathVariable Integer id, Model model) {

        User user = userService.findById(id);
        UserForm userForm = userService.convertToForm(user);

        model.addAttribute("userForm", userForm);
        return "user/edit";
    }

    // 会員情報編集処理
    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("userForm") @Validated({Default.class}) UserForm userForm,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("validationErrors", result);
            model.addAttribute("userForm", userForm);
            return "user/edit";
        }

        boolean success = userService.updateUser(userForm);

        if (!success) {
            model.addAttribute("accountError", E0016);
            model.addAttribute("userForm", userForm);
            return "user/edit";
        }

        return "redirect:/mypage";
    }

}
