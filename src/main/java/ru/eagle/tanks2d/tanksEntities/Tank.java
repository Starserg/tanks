package ru.eagle.tanks2d.tanksEntities;


import java.time.LocalTime;
import java.util.UUID;

public class Tank extends GameObject {

	public  Tank(String id, TankBody body, TankGun gun, Teams team, int x, int y){
	    this.enabled = true;
	    this.team = team;
	    this.x = x;
	    this.y = y;
	    if(team == Teams.First){
	    	this.direction = Directions.Top;
		}
		else{
	    	this.direction = Directions.Down;
		}
		this.id = UUID.fromString(id);
		//this.changeSpriteCounter = 0;
	    this.delta = 0;
		this.hp = body.getHp();
		this.tempHp = this.hp;
		this.speed = body.getSpeed();
		this.damage = gun.getDamage();
		this.timeToRecharge = gun.getTimeToRecharge();
		this.lastShoteTime = LocalTime.now();
		this.summOfDamage = 0;
		this.kills = 0;
	}

	private Teams team;
    private int hp;
    private int tempHp;
    private int damage;
    private int summOfDamage;
    private int kills;
    private long timeToRecharge;
    private LocalTime lastShoteTime;

	public Teams getTeam(){
	    return this.team;
    }

	//private int changeSpriteCounter;
	


	//public void setSpriteCounter(int c){
	//	this.changeSpriteCounter = c;
//	}

	//public int getChangeSpriteCounter(){
	//	return this.changeSpriteCounter;
	//}

	public int getHp() {
		return this.hp;
	}
	
	public int getTempHp() {
		return this.tempHp;
	}

	public long getTimeToRecharge() {
		return this.timeToRecharge;
	}
	

	public int getDamage() {
		return this.damage;
	}

    public int getKills() {
        return kills;
    }

    public int getSummOfDamage() {
        return summOfDamage;
    }

    public LocalTime getLastShoteTime() {
        return lastShoteTime;
    }

    public void setHp(int hp){
		this.tempHp = hp;
	}

	public void setSummOfDamage(int d){
		this.summOfDamage = d;
	}

	public void setLastShoteTime(LocalTime lastShoteTime) {
		this.lastShoteTime = lastShoteTime;
	}
}
