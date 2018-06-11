package ru.eagle.tanks2d.entities;

public class User {

    public User(String login, String email, String hash, String id){
        this.email = email;
        this.login = login;
        this.id = id;
        this.hash = hash;
    }





    private String id;

    private String login;

    private String email;

    private String hash;

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getHash() {
        return hash;
    }
}
