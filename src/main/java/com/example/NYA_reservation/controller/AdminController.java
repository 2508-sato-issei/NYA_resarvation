package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.RegularHolidayForm;
import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.controller.form.UserForm;
import com.example.NYA_reservation.dto.RestaurantReservationCountDto;
import com.example.NYA_reservation.security.LoginUserDetails;
import com.example.NYA_reservation.service.RegularHolidayService;
import com.example.NYA_reservation.service.RestaurantService;
import com.example.NYA_reservation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static com.example.NYA_reservation.validation.ErrorMessage.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    RestaurantService restaurantService;
    @Autowired
    RegularHolidayService regularHolidayService;
    @Autowired
    UserService userService;

    /*
     * サイト管理者画面表示
     */
    @GetMapping
    public ModelAndView show(){
        ModelAndView mav = new ModelAndView();
        //総会員数取得
        Long totalUsers = userService.countUsers();
        //総店舗数取得
        Long totalRestaurants = restaurantService.countRestaurants();
        //人気店舗取得
        List<RestaurantReservationCountDto> popularRestaurants = restaurantService.selectTopRestaurantsByReservationCount();


        mav.setViewName("admin/index");
        mav.addObject("totalUsers", totalUsers);
        mav.addObject("totalRestaurants", totalRestaurants);
        mav.addObject("popularRestaurants", popularRestaurants);
        return mav;
    }

    /*
     * 店舗一覧画面表示
     */
    @GetMapping("/restaurant-list")
    public ModelAndView showRestaurants(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        //店舗情報を取得
        List<RestaurantForm> restaurants = restaurantService.findAllRestaurants();

        //定休日情報を取得
        List<RegularHolidayForm> regularHolidays = regularHolidayService.findAllRegularHolidays();

        mav.setViewName("admin/restaurant/index");
        mav.addObject("restaurants", restaurants);
        mav.addObject("regularHolidays", regularHolidays);
        mav.addObject("currentUrl", request.getRequestURI());
        return mav;
    }

    /*
     * 店舗登録画面表示
     */
    @GetMapping("/restaurant/new")
    public ModelAndView newRestaurant(Model model){
        ModelAndView mav = new ModelAndView();

        if(!model.containsAttribute("formModel")){
            RestaurantForm restaurantForm = new RestaurantForm();
            mav.addObject("formModel", restaurantForm);
        }

        mav.setViewName("admin/restaurant/new");
        return mav;
    }

    /*
     * 店舗登録処理
     */
    @PostMapping("/restaurant/add")
    public ModelAndView addRestaurant(@ModelAttribute("formModel") @Validated RestaurantForm restaurantForm,
                                      BindingResult result,
                                      @RequestParam(value = "regularHoliday", required = false) List<Integer> regularHolidays,
                                      RedirectAttributes redirectAttributes){

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", restaurantForm);
            if(regularHolidays == null){
                redirectAttributes.addFlashAttribute("errorMessage", E0034);
            }
            return new ModelAndView("redirect:/admin/restaurant/new");
        } else {
            if(regularHolidays == null){
                redirectAttributes.addFlashAttribute("formModel", restaurantForm);
                redirectAttributes.addFlashAttribute("errorMessage", E0034);
                return new ModelAndView("redirect:/admin/restaurant/new");
            }
        }

        //店舗情報をDBに登録し、その情報を取得
        RestaurantForm savedRestaurant= restaurantService.addRestaurant(restaurantForm);

        //定休日、レストランIDをregularHolidaysFormにセット
        List<RegularHolidayForm> regularHolidayForms = new ArrayList<>();
        for(Integer regularHoliday : regularHolidays){
            RegularHolidayForm regularHolidayForm = new RegularHolidayForm();
            regularHolidayForm.setRegularHoliday(regularHoliday);
            regularHolidayForm.setRestaurantId(savedRestaurant.getId());
            regularHolidayForms.add(regularHolidayForm);
        }
        //定休日をDBに登録
        regularHolidayService.addRegularHolidays(regularHolidayForms);

        return new ModelAndView("redirect:/admin/restaurant-list");
    }

    /*
     * 店舗編集画面表示
     */
    @GetMapping("/restaurant/edit/{id}")
    public ModelAndView editRestaurant(Model model,
                                       @PathVariable("id")Integer id,
                                       @RequestParam(required = false) String referer,
                                       HttpSession session){

        ModelAndView mav = new ModelAndView();

        if(!model.containsAttribute("formModel")){
            //idで変更元のレストラン情報を取得
            RestaurantForm restaurant = restaurantService.findRestaurantById(id);
            mav.addObject("formModel", restaurant);
        }

        // referer をセッションに保存
        if (referer != null) {
            session.setAttribute("lastPage", referer);
        }

        mav.setViewName("admin/restaurant/edit");
        return mav;
    }

    /*
     * 店舗情報更新
     */
    @PutMapping("/restaurant/update/{id}")
    public ModelAndView updateRestaurant(@ModelAttribute("formModel") @Validated RestaurantForm restaurantForm,
                                         BindingResult result,
                                         @RequestParam(value = "regularHoliday", required = false) List<Integer> regularHolidays,
                                         RedirectAttributes redirectAttributes,
                                         @PathVariable("id") Integer id){

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", restaurantForm);
            if(regularHolidays == null){
                redirectAttributes.addFlashAttribute("errorMessage", E0034);
            }
            return new ModelAndView("redirect:/admin/restaurant/edit/" + id);
        } else {
            if(regularHolidays == null){
                redirectAttributes.addFlashAttribute("formModel", restaurantForm);
                redirectAttributes.addFlashAttribute("errorMessage", E0034);
                return new ModelAndView("redirect:/admin/restaurant/edit/" + id);
            }
        }

        restaurantForm.setId(id);

        //店舗情報をDBに登録し、その情報を取得
        RestaurantForm savedRestaurant= restaurantService.addRestaurant(restaurantForm);

        //元々の定休日情報を削除
        regularHolidayService.deleteByRestaurantId(id);

        //新しい定休日、レストランIDをregularHolidaysFormにセット
        List<RegularHolidayForm> regularHolidayForms = new ArrayList<>();
        for(Integer regularHoliday : regularHolidays){
            RegularHolidayForm regularHolidayForm = new RegularHolidayForm();
            regularHolidayForm.setRegularHoliday(regularHoliday);
            regularHolidayForm.setRestaurantId(savedRestaurant.getId());
            regularHolidayForms.add(regularHolidayForm);
        }
        //定休日をDBに登録
        regularHolidayService.addRegularHolidays(regularHolidayForms);

        return new ModelAndView("redirect:/admin/restaurant-list");

    }

    /*
     *店舗削除
     */

    @DeleteMapping("/restaurant/delete/{id}")
    public ModelAndView deleteRestaurant(@PathVariable("id") Integer id){
        //店舗情報を削除
        restaurantService.deleteRestaurant(id);
        //定休日情報も削除
        regularHolidayService.deleteByRestaurantId(id);
        return new ModelAndView("redirect:/admin/restaurant-list");
    }

    /*
     * ユーザー一覧表示
     */
    @GetMapping("/user-list")
    public ModelAndView showUsers(@AuthenticationPrincipal LoginUserDetails loginUser){
        ModelAndView mav = new ModelAndView();

        List<UserForm> users = userService.findAllUser();

        mav.setViewName("admin/user/index");
        mav.addObject("users", users);
        mav.addObject("loginUser", loginUser);
        return mav;
    }

    /*
     * ユーザー停止・有効切り替え
     */
    @PutMapping("/user/change/{id}")
    public ModelAndView changeIsStopped(@PathVariable("id") Integer id,
                                        @RequestParam("isStopped") boolean isStopped){
        userService.changeIsStopped(id, isStopped);

        return new ModelAndView("redirect:/admin/user-list");
    }
}
