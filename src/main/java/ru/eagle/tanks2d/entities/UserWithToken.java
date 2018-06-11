package ru.eagle.tanks2d.entities;

public class UserWithToken extends User {
    public UserWithToken(User u){
        super(u.getLogin(), u.getEmail(), u.getHash(), u.getId());
        this.token = new Token();
    }

    private Token token;

    public Token getToken(){
        return this.token;
    }
}
