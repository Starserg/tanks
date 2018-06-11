package ru.eagle.tanks2d.tanks2dBLL;

import ru.eagle.tanks2d.entities.User;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public abstract class AbstractBLL {

	protected AbstractBLL(int timeToUpdate) {
		updateTimer = new Timer();
	}

	protected Timer updateTimer;

	abstract void updateMap();
	
	abstract void loadMap(List<User> firstTeam, List<User> secondTeam);

	protected class UpdateTimerTask extends TimerTask{

		@Override
		public void run() {
			updateMap();
			
		}
		
	}
	
}
