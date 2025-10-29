package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.ReviewForm;
import com.example.NYA_reservation.repository.entity.RegularHoliday;
import com.example.NYA_reservation.repository.entity.Restaurant;
import com.example.NYA_reservation.repository.entity.User;
import com.example.NYA_reservation.security.LoginUserDetails;
import com.example.NYA_reservation.service.RegularHolidayService;
import com.example.NYA_reservation.service.RestaurantService;
import com.example.NYA_reservation.service.ReviewService;
import com.example.NYA_reservation.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    UserService userService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    RegularHolidayService regularHolidayService;
    @Autowired
    ReviewService reviewService;

    @GetMapping("/{id}")
    public String showRestaurantDetail(@PathVariable Integer id,
                                       @RequestParam(required = false) String referer,
                                       @RequestParam(required = false) String area,
                                       @RequestParam(required = false) String genre,
                                       @RequestParam(required = false) String headcount,
                                       HttpSession session,
                                       Model model) {

        Restaurant restaurant = restaurantService.findById(id);
        List<RegularHoliday> holidays = regularHolidayService.findByRestaurantId(id);

        // 数値から曜日文字列に変換
        List<String> holidayNames = holidays.stream()
                .map(h -> switch (h.getRegularHoliday()) {
                    case 1 -> "月曜日";
                    case 2 -> "火曜日";
                    case 3 -> "水曜日";
                    case 4 -> "木曜日";
                    case 5 -> "金曜日";
                    case 6 -> "土曜日";
                    case 7 -> "日曜日";
                    case 8 -> "祝日";
                    default -> "";
                })
                .toList();

        // referer をセッションに保存
        if (referer != null) {
            session.setAttribute("lastPage", referer);
        }

        model.addAttribute("area", area);
        model.addAttribute("genre", genre);
        model.addAttribute("headcount", headcount);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("holidayNames", holidayNames);
        model.addAttribute("reviews", reviewService.getReviewsByRestaurant(id));
        model.addAttribute("reviewForm", new ReviewForm());
        return "restaurant-detail";
    }

    @PostMapping("/{id}/reviews")
    public String postReview(@PathVariable Integer id,
                             @ModelAttribute ReviewForm reviewForm,
                             @AuthenticationPrincipal LoginUserDetails loginUser) {

        Restaurant restaurant = restaurantService.findById(id);
        User user = userService.findById(loginUser.getId());

        reviewForm.setId(null);
        reviewForm.setRestaurant(restaurant);
        reviewForm.setUser(user);

        reviewService.addReview(id, reviewForm);
        return "redirect:/restaurant/" + id;
    }

}
