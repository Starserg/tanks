package ru.eagle.tanks2d.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthorizationController {
    @GetMapping("/authorization")
    public String greeting(Model model) {
        return "authorization";
    }

}
