package ru.eagle.tanks2d.tanksEntities;

public class TankBody {

    public TankBody(int hp, double speed){
        this.hp = hp;
        this.speed = speed;
    }

    private int hp;

    private double speed;

    public int getHp() {
        return this.hp;
    }

    public double getSpeed() {
        return this.speed;
    }
}
