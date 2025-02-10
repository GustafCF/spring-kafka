package com.br.coffee_consumer_kafka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to the Coffee shop!");
        return "index";
    }

    @GetMapping("/productregister")
    public String showFormProduct() {
        return "product-register";
    }

    @GetMapping("/productlist")
    public String showProductList() {
        return "product-list";
    }

    @GetMapping("/orderlist")
    public String showOrderList() {
        return "order-list";
    }
}
