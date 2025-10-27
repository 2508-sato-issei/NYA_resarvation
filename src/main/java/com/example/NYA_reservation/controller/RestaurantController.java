package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.repository.entity.RegularHoliday;
import com.example.NYA_reservation.repository.entity.Restaurant;
import com.example.NYA_reservation.service.RegularHolidayService;
import com.example.NYA_reservation.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;
    @Autowired
    RegularHolidayService regularHolidayService;

    @GetMapping("/restaurant/{id}")
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
        return "restaurant-detail";
    }

}
