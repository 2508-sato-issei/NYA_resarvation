package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.service.ReservationService;
import com.example.NYA_reservation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;
    @Autowired
    RestaurantService restaurantService;

    /*
     * 予約画面を表示
     */
    @GetMapping("/new/{id}")
    public ModelAndView show(Model model,
                             @PathVariable("id") Integer restaurantId) {
        ModelAndView mav = new ModelAndView();

        RestaurantForm restaurant = restaurantService.findRestaurantById(restaurantId);

        if (!model.containsAttribute("formModel")) {
            //modelにformModelがない場合、空のFormをmavに保持させる(formModelが存在するとき=エラーでフォワード処理した時)
            ReservationForm reservationForm = new ReservationForm();
            mav.addObject("formModel", reservationForm);
        }
        mav.setViewName("reservation/new");
        mav.addObject("restaurant", restaurant);
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
                                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", reservationForm);
            return new ModelAndView("redirect:/reservation/new/" + restaurantId);
        }

        reservationForm.setRestaurantId(restaurantId);
        //一時的にuserIdは一律1で設定。
        // LoginUserDetailsが完成したらloginUser.getId();でuserIdを取得。
        reservationForm.setUserId(1);
        reservationService.saveReservation(reservationForm);

        //一時的にリダイレクト先をホーム画面に設定　マイページ完成後はマイページにリダイレクトする。
        return new ModelAndView("redirect:/");

    }

}
