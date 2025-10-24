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

        for (ReservationForm reservation : reservations) {
            LocalDate date = reservation.getReservationDate();
            if (!date.isBefore(LocalDate.now())) {
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
        mav.addObject("nextReservation", nextReservation);
        mav.addObject("currentUrl", request.getRequestURI());
        return mav;
    }

    @GetMapping("/history")
    public ModelAndView showHistory(@AuthenticationPrincipal LoginUserDetails loginUser) {
        ModelAndView mav = new ModelAndView("reservation/history");

        User user = userService.findById(loginUser.getId());
        List<ReservationForm> reservations = reservationService.findByUserId(loginUser.getId());
        List<ReservationForm> pastReservations = new ArrayList<>();

        LocalDate today = LocalDate.now();
        for (ReservationForm reservation : reservations) {
            LocalDate date = reservation.getReservationDate();
            if (date.isBefore(today)) {
                pastReservations.add(reservation);
            }
        }
        // 日付の新しい順にソート（最近の履歴を上に）
        pastReservations.sort(Comparator
                .comparing(ReservationForm::getReservationDate)
                .reversed());
        // 表示用データをセット
        mav.addObject("user", user);
        mav.addObject("pastReservations", pastReservations);
        mav.addObject("loginUser", loginUser);
        return mav;
    }
}
