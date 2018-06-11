package ru.eagle.tanks2d.entities;

import java.util.UUID;

public class UserStatistic {

    public UserStatistic(UUID userId, int battles, int wins, int kills, int mediumDamage){
        this.userId = userId;
        this.mediumDamage = mediumDamage;
        this.battles = battles;
        this.kills = kills;
        if(battles>0){
            this.winsStatistic = ((double)wins)/battles;
        }
        else{
            this.winsStatistic = 0;
        }
        this.userLogin = "not found";
    }

    private String userLogin;
    private double winsStatistic;
    private UUID userId;
    private int battles;
    private int kills;
    private int mediumDamage;

    public int getMediumDamage() {
        return mediumDamage;
    }

    public void setMediumDamage(int md){
        this.mediumDamage = md;
    }

    public void setWinsStatistic(double winsStatistic) {
        this.winsStatistic = winsStatistic;
    }

    public void addKills(int k){
        this.kills = this.kills + k;
    }

    public void addBattle(){
        this.battles = this.battles +1;
    }

    public int getKills() {
        return kills;
    }

    public int getBattles() {
        return battles;
    }


    public void setUserLogin(String userLogin){
        this.userLogin = userLogin;
    }


    public UUID getUserId(){
        return this.userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public double getWinsStatistic() {
        return winsStatistic;
    }


}
