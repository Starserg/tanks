package ru.eagle.tanks2d.entities;



import ru.eagle.tanks2d.BLL.BLL;

import java.time.LocalDateTime;
import java.util.UUID;
public class Token {


    private final int LIFETIME = 10;

    private final String STRING_VALUE = UUID.randomUUID().toString();
    private LocalDateTime expires;

    public Token() {
        this.expires = LocalDateTime.now().plusMinutes(LIFETIME);
    }

    public String getStringToken() {
        if (expires.isAfter(LocalDateTime.now())) {
            this.expires = LocalDateTime.now().plusMinutes(LIFETIME);
            return STRING_VALUE;
        } else {
            BLL.INSTANCE.removeToken(this);
            return "";
        }
    }
}
