package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.RegularHolidayForm;
import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.repository.entity.Restaurant;
import com.example.NYA_reservation.security.LoginUserDetails;
import com.example.NYA_reservation.service.RegularHolidayService;
import com.example.NYA_reservation.service.ReservationService;
import com.example.NYA_reservation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.NYA_reservation.validation.ErrorMessage.*;

@Controller
public class ReservationEditController {

    @Autowired
    ReservationService reservationService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    RegularHolidayService regularHolidayService;

    //予約変更画面表示
    @GetMapping("/reservation/edit/{id}")
    public ModelAndView reservationEdit(
            @PathVariable String id,
            @ModelAttribute("formModel") ReservationForm form,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal LoginUserDetails loginUser) {

        List<String> errorMessages = new ArrayList<>();

        //取得したユーザーIDをチェック
        if(id == null || id.isEmpty() || !id.matches("^[0-9]+$")){
            errorMessages.add(E0011);
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/mypage");
        }

        ModelAndView mav = new ModelAndView("reservation/edit");

        if (form == null || form.getReservationDate() == null) {
            ReservationForm reservation = reservationService.findById(Integer.parseInt(id));
            if (reservation == null) {
                errorMessages.add(E0011);
                redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
                return new ModelAndView("redirect:/mypage");
            }
            mav.addObject("formModel", reservation);
                // 店舗情報を取得
            Restaurant restaurant = restaurantService.findById(reservation.getRestaurantId());
            if (restaurant == null) {
                errorMessages.add(E0011);
                redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
                return new ModelAndView("redirect:/mypage");
            }
            mav.addObject("restaurant", restaurant);
            List<RegularHolidayForm> regularHolidays =
                    regularHolidayService.findRegularHolidaysByRestaurantId(reservation.getRestaurantId());
            mav.addObject("regularHolidays", regularHolidays);
        } else {
            // 予約情報を取得
            Restaurant restaurant = restaurantService.findById(form.getRestaurantId());
            mav.addObject("formModel", form);
            mav.addObject("restaurant", restaurant);
        }

        mav.addObject("loginUser", loginUser);
        return mav;
    }

    //予約変更機能
    @PutMapping("/reservation/update/{id}")
    public ModelAndView updateReservation(
            @PathVariable("id") Integer id,
            @AuthenticationPrincipal LoginUserDetails loginUser,
            @Validated @ModelAttribute("formModel") ReservationForm reservationForm,
            BindingResult result,
            RedirectAttributes redirectAttributes){

        List<String> errorMessages = new ArrayList<>();
        //店舗の定員を取得
        RestaurantForm restaurant = restaurantService.findRestaurantById(reservationForm.getRestaurantId());
        Integer capacity = restaurant.getCapacity();
        //入力された予約人数を取得
        Integer headcount = reservationForm.getHeadcount();
        //入力された予約日を取得
        LocalDate reservationDate = reservationForm.getReservationDate();
        //本日の日付、６０日後の日付を取得
        LocalDate now = LocalDate.now();
        LocalDate future = now.plusDays(60);
        // 定休日一覧を取得（サービス層から）
        List<RegularHolidayForm> regularHolidays =
                reservationService.getRegularHolidaysByRestaurantId(reservationForm.getRestaurantId());

        if (reservationDate != null) {

            // --- 定休日チェック ---
            int intDayOfWeek = reservationDate.getDayOfWeek().getValue();
            if (regularHolidays != null && !regularHolidays.isEmpty()) {
                for (RegularHolidayForm regularHoliday : regularHolidays) {
                    if ((regularHoliday.getRegularHoliday() != null) &&
                            (regularHoliday.getRegularHoliday() == intDayOfWeek) && (!reservationDate.isBefore(now))) {
                        result.addError(new FieldError(result.getObjectName(), "reservationDate",
                                reservationDate, false, null, null, E0036
                        ));
                        break;
                    }
                }
            }
            // --- 過去日／60日超過チェック ---
            if (reservationDate.isBefore(now) || reservationDate.isAfter(future)) {
                result.addError(new FieldError(result.getObjectName(), "reservationDate",
                        reservationDate, false, null, null, E0007
                ));
            }
        }

        //必須チェック
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));
        }

        //予約人数チェック
        if((headcount != null) && (headcount > capacity)){
            errorMessages.add(E0009);
        } else if ((headcount != null) && (headcount == 0)) {
            errorMessages.add(E0008);
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            redirectAttributes.addFlashAttribute("formModel", reservationForm);
            return new ModelAndView("redirect:/reservation/edit/" + id);
        }

        reservationService.updateReservation(reservationForm);

        redirectAttributes.addFlashAttribute("successMessage", "予約が変更されました。");
        return new ModelAndView("redirect:/mypage");
    }

    //予約キャンセル
    @DeleteMapping("/reservation/cancel/{id}")
    public ModelAndView cancelReservation(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        reservationService.deleteReservation(id);
        redirectAttributes.addFlashAttribute("successMessage", "✔️予約をキャンセルしました。");
        return new ModelAndView("redirect:/mypage");
    }
}
