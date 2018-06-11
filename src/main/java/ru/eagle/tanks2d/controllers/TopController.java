package ru.eagle.tanks2d.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {
    @GetMapping("/top")
    public String greeting(Model model) {
        return "top";
    }
}