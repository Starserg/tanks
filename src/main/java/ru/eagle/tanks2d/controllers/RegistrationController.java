package ru.eagle.tanks2d.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.eagle.tanks2d.BLL.BLL;
import ru.eagle.tanks2d.service.UserService;

import java.util.Map;
import java.util.UUID;


@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("message", " ");
        return "registration";
    }

    @GetMapping("/activation/{code}")
    public String activate(Model model, @PathVariable String code) {
        if(BLL.INSTANCE.activate(code)){
            model.addAttribute("message", "аккаунт активирован");
        }
        else{
            model.addAttribute("message", "активационный код не найден");
        }
        return "registration";
    }


    @PostMapping("/registrate")
    public String registrate(@RequestParam String email, @RequestParam String login, @RequestParam String password, Map<String, Object> model){
        if(BLL.INSTANCE.isUserDataValid(email, login, password)){
            if(!BLL.INSTANCE.isLoginFree(login)){
                model.put("message", "логин занят");
                return "registration";
            }
            else if(!BLL.INSTANCE.isEmailFree(email)){
                model.put("message", "почта занята");
                return "registration";
            }
            else{
                String activationCode = UUID.randomUUID().toString();
                BLL.INSTANCE.addUser(email, login, password, activationCode);
                userService.sendMail(email, "Если вы регистрировали аккаунт в игре tanks2d, перейдите по ссылке: http://localhost:8080/activation/", activationCode);
                model.put("message", "ссылка активации отправлена на почту");
                return "registration";
            }
        }
        else {
            model.put("message", "некорректные данные");
            return "registration";
        }
    }
}