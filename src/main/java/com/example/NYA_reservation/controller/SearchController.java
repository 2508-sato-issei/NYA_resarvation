package com.example.NYA_reservation.controller;

import com.example.NYA_reservation.controller.form.SearchForm;
import com.example.NYA_reservation.repository.entity.Restaurant;
import com.example.NYA_reservation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/search")
    public String searchRestaurants(@ModelAttribute SearchForm searchForm,
                                    @RequestParam(defaultValue = "0") int page,
                                    Model model) {

        Page<Restaurant> resultPage = restaurantService.searchRestaurants(searchForm, PageRequest.of(page, 10));

        model.addAttribute("searchForm", searchForm);
        model.addAttribute("results", resultPage.getContent());
        model.addAttribute("page", resultPage);
        return "search-result";
    }

}
