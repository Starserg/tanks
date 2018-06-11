package ru.eagle.tanks2d.tanksEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Map {

	public Map(Space[][] spaces) {
		tanksOutOfGame = new ArrayList<>();
		this.spaces = spaces;
		tanks = new ArrayList<Tank>();
		bullets = new ArrayList<Bullet>();
		for(int i = 0; i < spaces.length; i ++){
			for(int j = 0; j < spaces[i].length; j++){
				if(spaces[i][j].getGameObject() != null && spaces[i][j].getGameObject() instanceof Tank){
					tanks.add((Tank)(spaces[i][j].getGameObject()));
				}
			}
		}
	}


    public ArrayList<Tank> getTanksOutOfGame() {
        return tanksOutOfGame;
    }

    private Space[][] spaces;


	private ArrayList<Tank> tanks;

	private ArrayList<Bullet> bullets;

	public Space[][] getSpaces(){
		return this.spaces;
	}
	
	public ArrayList<Tank> getTanks() {
		return this.tanks;
	}


	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}


	public boolean canObjGoThere(int x0, int y0, Directions direction) {
	    int x = x0;
	    int y = y0;
	    if(direction == Directions.Top){
	        y--;
        }
        else if(direction == Directions.Right){
	        x++;
        }
        else if(direction == Directions.Down){
	        y++;
        }
        else{
	        x--;
        }
	    if(x< 0 || y < 0 || x >= spaces.length || y >= spaces[0].length){
	        return false;
        }
        return (spaces[x][y].getGameObject() == null);
    }


	public Tank getTankById(String id){
		for(int i = 0; i < tanks.size(); i++){
			if(tanks.get(i).getId().toString().equals(id)){
			    return tanks.get(i);
            }
		}
		return null;
	}



	public void addTankToOutList(Tank tank){
		if(tank!= null){
			tanksOutOfGame.add(tank);
		}
	}

	private ArrayList<Tank> tanksOutOfGame;
}
