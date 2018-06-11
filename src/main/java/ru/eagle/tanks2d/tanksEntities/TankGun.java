package ru.eagle.tanks2d.tanksEntities;




public class TankGun {

    public TankGun(int damage, long timeToRecharge){
        this.damage = damage;
        this.timeToRecharge = timeToRecharge;

    }


    private long timeToRecharge;

    public long getTimeToRecharge() {
        return this.timeToRecharge;
    }

    private int damage;

    public int getDamage() {
        return this.damage;
    }
}
