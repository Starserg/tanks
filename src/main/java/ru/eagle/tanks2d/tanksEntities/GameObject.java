package ru.eagle.tanks2d.tanksEntities;

import java.util.UUID;

public abstract class GameObject {

	protected int x;
	protected int y;
	protected double delta;
	protected double speed;
	protected Directions direction;
	protected boolean enabled;
	protected UUID id;


	public void setX(int x) throws Exception {
	    if(x>= 0) {
            this.x = x;
            }
            else{
	        throw new Exception();
        }
    }

    public void setY(int y) throws Exception {
        if(y>= 0) {
            this.y = y;
        }
        else{
            throw new Exception();
        }
    }


    public int getX() {
        return this.x;
    }
	
	public int getY() {
		return this.y;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	
	public Directions getDirection() {
		return this.direction;
	}


	public void setDirection(Directions dir){
	    this.direction = dir;
    }

	public boolean getEnabled() {
		return this.enabled;
	}

	public double getDelta(){return this.delta;}

	public void setDelta(double newDelta){
	    this.delta = newDelta;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UUID getId(){
		return this.id;
	}
}
