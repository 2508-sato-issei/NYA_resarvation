package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.repository.entity.User;
import com.example.NYA_reservation.security.LoginUserDetails;
import com.example.NYA_reservation.service.ReservationService;
import com.example.NYA_reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MypageController {

    @Autowired
    ReservationService reservationService;
    @Autowired
    UserService userService;

    @GetMapping("/mypage")
    public ModelAndView showMypage(@AuthenticationPrincipal LoginUserDetails loginUser){
        ModelAndView mav = new ModelAndView("mypage");

        List<ReservationForm> reservations = reservationService.findByUserId(loginUser.getId());
        User user = userService.findById(loginUser.getId());
        Map<Integer, Boolean> nowReservations = new HashMap<>();
        for (ReservationForm reservation : reservations) {
            boolean already = reservationService.isAlreadyReservation(reservation);
            nowReservations.put(reservation.getId(), already);
        }
        ReservationForm nextReservation = reservations.stream()
                .filter(r -> !r.getReservationDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(ReservationForm::getReservationDate)
                        .thenComparing(ReservationForm::getReservationTime))
                .findFirst()
                .orElse(null);
        mav.addObject("user", user);
        mav.addObject("reservations", reservations);
        mav.addObject("isAlreadyReservation", nowReservations);
        mav.addObject("loginUser", loginUser);
        mav.addObject("nextReservation", nextReservation);
        return mav;
    }
}
