package ru.eagle.tanks2d.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private MailSender mailSender;


    public void sendMail(String email, String message, String code){
        mailSender.send(email, "Сообщение от tanks2d", message + code);
    }
}