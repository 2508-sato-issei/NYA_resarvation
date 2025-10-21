package com.example.NYA_reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView showHome() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/index");
        return mav;
    }
}
