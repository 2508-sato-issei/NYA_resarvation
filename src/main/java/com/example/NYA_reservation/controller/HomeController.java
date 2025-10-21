package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.dto.AreaReservationCountDto;
import com.example.NYA_reservation.dto.GenreReservationCountDto;
import com.example.NYA_reservation.dto.RestaurantReservationCountDto;
import com.example.NYA_reservation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/")
    public ModelAndView showHome() {

        List<AreaReservationCountDto> popularAreas = restaurantService.selectTopAreasByReservationCount();
        List<GenreReservationCountDto> popularGenres = restaurantService.selectTopGenresByReservationCount();
        List<RestaurantReservationCountDto> popularRestaurants = restaurantService.selectTopRestaurantsByReservationCount();

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("popularAreas", popularAreas);
        mav.addObject("popularGenres", popularGenres);
        mav.addObject("popularRestaurants", popularRestaurants);
        return mav;
    }

}
