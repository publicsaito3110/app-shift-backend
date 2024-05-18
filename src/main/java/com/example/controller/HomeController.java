package com.example.controller;

import com.example.domain.model.bean.HomeBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public HomeBean home() {
        return new HomeBean("A001", "田中");
    }
}
