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

        int totalPages = resultPage.getTotalPages();
        int currentPage = resultPage.getNumber();
        int displayRange = 5;

        int startPage = Math.max(0, currentPage - displayRange);
        int endPage = Math.min(totalPages - 1, currentPage + displayRange);

        boolean showFirst = startPage > 0;
        boolean showLast = endPage < totalPages - 1;

        // 検索結果
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("results", resultPage.getContent());

        // ページネーション
        model.addAttribute("page", resultPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("showFirst", showFirst);
        model.addAttribute("showLast", showLast);

        return "search-result";
    }

}
