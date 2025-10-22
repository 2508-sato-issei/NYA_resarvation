package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.RegularHolidaysForm;
import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.repository.entity.RegularHolidays;
import com.example.NYA_reservation.security.LoginUserDetails;
import com.example.NYA_reservation.service.RegularHolidaysService;
import com.example.NYA_reservation.service.ReservationService;
import com.example.NYA_reservation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    RegularHolidaysService regularHolidaysService;

    /*
     * 予約画面を表示
     */
    @GetMapping("/new/{id}")
    public ModelAndView show(Model model,
                             @PathVariable("id") Integer restaurantId){
        ModelAndView mav = new ModelAndView();

        //IDで店舗情報を取得
        RestaurantForm restaurant =  restaurantService.findRestaurantById(restaurantId);

        //restaurantIdで定休日情報を取得
        List<RegularHolidaysForm> regularHolidays = regularHolidaysService.findRegularHolidaysByRestaurantId(restaurantId);

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

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", reservationForm);
            return new ModelAndView("redirect:/reservation/new/" + restaurantId);
        }

        //userIdとrestaurantIdをFormに設定
        reservationForm.setRestaurantId(restaurantId);
        reservationForm.setUserId(loginUser.getId());

        reservationService.saveReservation(reservationForm);

        //一時的にリダイレクト先をホーム画面に設定　マイページ完成後はマイページにリダイレクトする。
        return new ModelAndView("redirect:/");

    }

}
