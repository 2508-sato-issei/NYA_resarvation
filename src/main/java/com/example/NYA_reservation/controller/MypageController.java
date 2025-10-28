package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.repository.entity.User;
import com.example.NYA_reservation.security.LoginUserDetails;
import com.example.NYA_reservation.service.ReservationService;
import com.example.NYA_reservation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class MypageController {

    @Autowired
    ReservationService reservationService;
    @Autowired
    UserService userService;

    @GetMapping("/mypage")
    public ModelAndView showMypage(@AuthenticationPrincipal LoginUserDetails loginUser,
                                   HttpServletRequest request){
        ModelAndView mav = new ModelAndView("mypage");

        List<ReservationForm> reservations = reservationService.findByUserId(loginUser.getId());
        User user = userService.findById(loginUser.getId());
        List<ReservationForm> futureReservations = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (ReservationForm reservation : reservations) {
            LocalDateTime reservationDateTime = LocalDateTime.of(
                    reservation.getReservationDate(),
                    reservation.getReservationTime());
            if (reservationDateTime.isAfter(now)) {
                futureReservations.add(reservation);
            }
        }
        futureReservations.sort(Comparator
                .comparing(ReservationForm::getReservationDate)
                .thenComparing(ReservationForm::getReservationTime));

        // 当日の変更・キャンセル制御
        Map<Integer, Boolean> nowReservations = new HashMap<>();
        for (ReservationForm reservation : futureReservations) {
            boolean already = reservationService.isAlreadyReservation(reservation);
            nowReservations.put(reservation.getId(), already);
        }

        // 最も近い予約（次の予約）を取得
        ReservationForm nextReservation = null;
        if (!futureReservations.isEmpty()) {
            nextReservation = futureReservations.get(0);
        }

        mav.addObject("user", user);
        mav.addObject("reservations", futureReservations);
        mav.addObject("isAlreadyReservation", nowReservations);
        mav.addObject("nextReservation", nextReservation);
        mav.addObject("loginUser", loginUser);
        mav.addObject("currentUrl", request.getRequestURI());
        return mav;
    }

    @GetMapping("/history")
    public ModelAndView showHistory(@AuthenticationPrincipal LoginUserDetails loginUser) {
        ModelAndView mav = new ModelAndView("reservation/history");

        User user = userService.findById(loginUser.getId());
        List<ReservationForm> reservations = reservationService.findByUserId(loginUser.getId());
        List<ReservationForm> pastReservations = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (ReservationForm reservation : reservations) {
            LocalDateTime reservationDateTime = LocalDateTime.of(
                    reservation.getReservationDate(),
                    reservation.getReservationTime());
            if (reservationDateTime.isBefore(now)) {
                pastReservations.add(reservation);
            }
        }
        // 日付の新しい順にソート（最近の履歴を上に）
        pastReservations.sort(Comparator
                .comparing(ReservationForm::getReservationDate)
                .reversed());

        mav.addObject("user", user);
        mav.addObject("pastReservations", pastReservations);
        mav.addObject("loginUser", loginUser);
        return mav;
    }
}
