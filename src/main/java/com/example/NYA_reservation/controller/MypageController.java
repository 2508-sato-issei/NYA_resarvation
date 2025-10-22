package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.security.LoginUserDetails;
import com.example.NYA_reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MypageController {

    @Autowired
    ReservationService reservationService;

    @GetMapping("/mypage")
    public ModelAndView showMypage(@AuthenticationPrincipal LoginUserDetails loginUser){
        ModelAndView mav = new ModelAndView("mypage");

        List<ReservationForm> reservations = reservationService.findByUserId(loginUser.getId());
        Map<Integer, Boolean> nowReservations = new HashMap<>();
        for (ReservationForm reservation : reservations) {
            boolean already = reservationService.isAlreadyReservation(reservation);
            nowReservations.put(reservation.getId(), already);
        }
        mav.addObject("reservations", reservations);
        mav.addObject("isAlreadyReservation", nowReservations);
        mav.addObject("loginUser", loginUser);
        return mav;
    }
}
