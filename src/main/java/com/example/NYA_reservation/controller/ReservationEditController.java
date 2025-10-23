package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.repository.entity.Restaurant;
import com.example.NYA_reservation.security.LoginUserDetails;
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

    //予約変更画面表示
    @GetMapping("/reservation/edit/{id}")
    public ModelAndView reservationEdit(@PathVariable String id, RedirectAttributes redirectAttributes,
                                        @AuthenticationPrincipal LoginUserDetails loginUser){

        List<String> errorMessages = new ArrayList<>();

        //取得したユーザーIDをチェック
        if(id == null || id.isEmpty() || !id.matches("^[0-9]+$")){
            errorMessages.add(E0011);
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/mypage");
        }

        //予約情報取得
        ReservationForm reservation = reservationService.findById(Integer.parseInt(id));
        if(reservation == null){
            errorMessages.add(E0011);
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/mypage");
        }

        //店舗情報取得
        Restaurant restaurant = restaurantService.findById(reservation.getRestaurantId());
        if (restaurant == null) {
            errorMessages.add(E0011);
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/mypage");
        }

        ModelAndView mav = new ModelAndView("reservation/edit");
        mav.addObject("formModel", reservation);
        mav.addObject("restaurant", restaurant);
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
        //入力された予約日を取得
        LocalDate reservationDate = reservationForm.getReservationDate();
        //本日の日付、６０日後の日付を取得
        LocalDate now = LocalDate.now();
        LocalDate future = now.plusDays(60);

        if (reservationDate != null) {
            if (reservationDate.isBefore(now)) {
                result.addError(new FieldError(
                        result.getObjectName(), "reservationDate",
                        reservationDate, false, null, null, E0007));
            } else if (reservationDate.isAfter(future)) {
                result.addError(new FieldError(
                        result.getObjectName(), "reservationDate",
                        reservationDate, false, null, null, E0007));
            }
        }

        //必須チェック
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));
        }

        //定員を超える場合
        if (reservationForm.getRestaurantId() != null && reservationForm.getHeadcount() != null) {
            Integer capacity = reservationService.getRestaurantCapacity(reservationForm.getRestaurantId());
            if (reservationForm.getHeadcount() > capacity) {
                errorMessages.add(E0009);
            }
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            redirectAttributes.addFlashAttribute("formModel", reservationForm);
            return new ModelAndView("redirect:/reservation/edit/" + id);
        }

        reservationService.updateReservation(reservationForm);
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
