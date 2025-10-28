package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.SearchForm;
import com.example.NYA_reservation.dto.AreaReservationCountDto;
import com.example.NYA_reservation.dto.GenreReservationCountDto;
import com.example.NYA_reservation.dto.RestaurantReservationCountDto;
import com.example.NYA_reservation.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/")
    public String showHome(HttpSession session, Model model) {

        //CustomCustomAccessDeniedHandlerで出たエラーを拾う
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        List<AreaReservationCountDto> popularAreas = restaurantService.selectTopAreasByReservationCount();
        List<GenreReservationCountDto> popularGenres = restaurantService.selectTopGenresByReservationCount();
        List<RestaurantReservationCountDto> popularRestaurants = restaurantService.selectTopRestaurantsByReservationCount();

        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("popularAreas", popularAreas);
        model.addAttribute("popularGenres", popularGenres);
        model.addAttribute("popularRestaurants", popularRestaurants);
        return "index";
    }

}
