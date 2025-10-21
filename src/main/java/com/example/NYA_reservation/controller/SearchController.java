package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.SearchForm;
import com.example.NYA_reservation.repository.entity.Restaurant;
import com.example.NYA_reservation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/search")
    public String searchRestaurants(@ModelAttribute SearchForm searchForm, Model model) {
        List<Restaurant> results = restaurantService.searchRestaurants(searchForm);
        model.addAttribute("results", results);
        return "search-result";
    }

}
