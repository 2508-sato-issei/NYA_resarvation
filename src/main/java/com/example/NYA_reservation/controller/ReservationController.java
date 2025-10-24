package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.RegularHolidayForm;
import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.security.LoginUserDetails;
import com.example.NYA_reservation.service.RegularHolidayService;
import com.example.NYA_reservation.service.ReservationService;
import com.example.NYA_reservation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

import static com.example.NYA_reservation.validation.ErrorMessage.*;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    RegularHolidayService regularHolidayService;

    /*
     * 予約画面を表示
     */
    @GetMapping("/new/{id}")
    public ModelAndView show(Model model,
                             @PathVariable("id") Integer restaurantId) {
        ModelAndView mav = new ModelAndView();

        //IDで店舗情報を取得
        RestaurantForm restaurant = restaurantService.findRestaurantById(restaurantId);

        //restaurantIdで定休日情報を取得
        List<RegularHolidayForm> regularHolidays = regularHolidayService.findRegularHolidaysByRestaurantId(restaurantId);

        if(!model.containsAttribute("formModel")){
            //modelにformModelがない場合、空のFormをmavに保持させる(formModelが存在するとき=エラーでフォワード処理した時)
            ReservationForm reservationForm = new ReservationForm();
            mav.addObject("formModel", reservationForm);
        }
        mav.setViewName("reservation/new");
        mav.addObject("restaurant", restaurant);
        mav.addObject("regularHolidays", regularHolidays);
        mav.addObject("restaurantId", restaurantId);
        return mav;
    }

    /*
     * 予約処理
     */
    @PostMapping("/add/{id}")
    public ModelAndView addReservation(@ModelAttribute("formModel") @Validated ReservationForm reservationForm,
                                       BindingResult result,
                                       @PathVariable("id") Integer restaurantId,
                                       RedirectAttributes redirectAttributes,
                                       @AuthenticationPrincipal LoginUserDetails loginUser){

        //店舗の定員を取得
        RestaurantForm restaurant = restaurantService.findRestaurantById(restaurantId);
        Integer capacity = restaurant.getCapacity();
        //入力された予約人数を取得
        Integer headcount = reservationForm.getHeadcount();
        //入力された予約日を取得
        LocalDate reservationDate = reservationForm.getReservationDate();
        //店舗の定休日情報を取得
        List<RegularHolidayForm> regularHolidays = regularHolidayService.findRegularHolidaysByRestaurantId(restaurantId);
        //本日の日付、６０日後の日付を取得
        LocalDate now = LocalDate.now();
        LocalDate future = now.plusDays(60);


        if(reservationDate != null){
            //入力された予約日の曜日（インデックス）を取得
            int intDayOfWeek = reservationDate.getDayOfWeek().getValue();
            for(RegularHolidayForm regularHoliday : regularHolidays){
                //定休日チェック
                if(regularHoliday.getRegularHoliday() == intDayOfWeek){
                    FieldError error = new FieldError(result.getObjectName(),
                            "reservationDate", reservationDate,
                            false,
                            null,
                            null, E0036);
                    result.addError(error);
                }
            }
            //予約日が過去じゃないor２か月以内かチェック
            if(reservationDate.isAfter(future) || reservationDate.isBefore(now)){
                FieldError error = new FieldError(result.getObjectName(),
                        "reservationDate", reservationDate,
                        false,
                        null,
                        null, E0007);
                result.addError(error);
            }
        }

        //予約人数チェック
        if((headcount != null) && (headcount > capacity)){
            FieldError error = new FieldError(result.getObjectName(),
                    "headcount", headcount,
                    false,
                    null,
                    null,
                    E0009);
            result.addError(error);
        } else if ((headcount != null) && (headcount == 0)) {
            FieldError error = new FieldError(result.getObjectName(),
                    "headcount", headcount,
                    false,
                    null,
                    null,
                    E0008);
            result.addError(error);
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", reservationForm);
            return new ModelAndView("redirect:/reservation/new/" + restaurantId);
        }

        //userIdとrestaurantIdをFormに設定
        reservationForm.setRestaurantId(restaurantId);
        reservationForm.setUserId(loginUser.getId());

        reservationService.saveReservation(reservationForm);

        return new ModelAndView("redirect:/mypage");

    }

}
