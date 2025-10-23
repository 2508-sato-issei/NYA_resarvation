package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.repository.entity.RegularHoliday;
import com.example.NYA_reservation.repository.entity.Restaurant;
import com.example.NYA_reservation.service.RegularHolidayService;
import com.example.NYA_reservation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;
    @Autowired
    RegularHolidayService regularHolidayService;

    @GetMapping("/restaurant/{id}")
    public String showRestaurantDetail(@PathVariable Integer id, Model model) {

        Restaurant restaurant = restaurantService.findById(id);
        List<RegularHoliday> holidays = regularHolidayService.findByRestaurantId(id);

        // 数値から曜日文字列に変換
        List<String> holidayNames = holidays.stream()
                .map(h -> switch (h.getRegularHoliday()) {
                    case 1 -> "月";
                    case 2 -> "火";
                    case 3 -> "水";
                    case 4 -> "木";
                    case 5 -> "金";
                    case 6 -> "土";
                    case 7 -> "日";
                    case 8 -> "祝日";
                    default -> "";
                })
                .toList();

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("holidayNames", holidayNames);
        return "restaurant-detail";
    }

}
