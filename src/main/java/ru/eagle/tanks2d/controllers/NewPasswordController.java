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
public class NewPasswordController {
    @Autowired
    private UserService userService;

    @GetMapping("/newpassword1")
    public String newPassword1(Model model) {
        return "newpassword1";
    }

    @GetMapping("/newpassword/{code}")
    public String newPassword2(Model model, @PathVariable String code) {
        if(BLL.INSTANCE.checkNewPassworCode(code)){
            model.addAttribute("npcode", code);
            return "newpassword";
        }
        else{
            model.addAttribute("message", "код смены пароля не найден");
            return "registration";
        }
    }


    @PostMapping("/newpassword1")
    public String newPassword1Post(@RequestParam String email, Map<String, Object> model){
        if(BLL.INSTANCE.isEmailValid(email) && !BLL.INSTANCE.isEmailFree(email)){
            String newPasswordCode = UUID.randomUUID().toString();
            if(BLL.INSTANCE.setNewPasswordCodeToUser(email, newPasswordCode)){
                userService.sendMail(email, "Восстановления пароля: http://localhost:8080/newpassword/",newPasswordCode);
                model.put("message", "ссылка активации отправлена на почту");
            }
            else{
                model.put("message", "некорректный ввод почты");
            }
            return "registration";

        }
        else {
            model.put("message", "некорректный ввод почты");
            return "registration";
        }
    }

    @PostMapping("/newpasswordSet")
    public String newPassword2Post(@RequestParam String code, @RequestParam String newpassword, Map<String, Object> model){
        if(BLL.INSTANCE.checkNewPassworCode(code)){
            if(BLL.INSTANCE.isPasswordValid(newpassword)){
                if(BLL.INSTANCE.replacePassword(code, newpassword)){
                    model.put("message", "пароль успешно обновлен, введите его при авторизации");
                }
                else {
                    model.put("message", "не удалось сменить пароль");
                }
            }
            else {
                model.put("message", "некорректный пароль, не удалось сменить пароль");
            }
        }
        else{
            model.put("message", "код смены пароля не найден");
        }
        return "registration";
    }

}


