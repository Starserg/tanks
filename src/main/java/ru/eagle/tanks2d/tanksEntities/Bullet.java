package ru.eagle.tanks2d.tanksEntities;

import java.util.UUID;

public class Bullet extends GameObject {

	public Bullet(Tank tank, int x, int y){
		this.tank = tank;
		this.direction = tank.getDirection();
		this.speed = 0.5; //bulletSpeed
		this.x = x;
		this.y = y;
		this.enabled = true;
		this.delta = 0;
		this.id = tank.getId();
	}

	private Tank tank;
	
	public Tank getTank() {
		return this.tank;
	}
	
	
}
