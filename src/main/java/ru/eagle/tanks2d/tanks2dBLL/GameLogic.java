package ru.eagle.tanks2d.tanks2dBLL;


import ru.eagle.tanks2d.entities.User;
import ru.eagle.tanks2d.tanks2dDAL.MapMaker;
import ru.eagle.tanks2d.tanksEntities.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameLogic extends AbstractBLL {


	public GameLogic(int timeToUpdate, List<User> firstTeam, List<User> secondTeam){
		super(timeToUpdate);
		loadMap(firstTeam, secondTeam);
		updateTimer.schedule(new UpdateTimerTask(), timeToUpdate, timeToUpdate);
		removeList = new ArrayList<>();
	}

	private Map map;
	private ArrayList<Bullet> removeList;

	private void upDateBullets(){
	    removeList.clear();
		for (Bullet item: map.getBullets()){
            if(map.getSpaces()[item.getX()][item.getY()].getGameObject() != null){
                bulletFoundTheObj(item, map.getSpaces()[item.getX()][item.getY()].getGameObject());
                continue;
            }
			if(item.getEnabled()){
				if(map.canObjGoThere(item.getX(), item.getY(), item.getDirection())){
					item.setEnabled(false);
				}
				else if(item.getX() + getDx(item.getDirection())< 0 || item.getY() + getDy(item.getDirection()) < 0 ||
                        item.getX() + getDx(item.getDirection())>= map.getSpaces().length ||
                        item.getY() + getDy(item.getDirection())>= map.getSpaces()[0].length){
				    removeList.add(item);
                }
                else{
				    bulletFoundTheObj(item, map.getSpaces()[item.getX() + getDx(item.getDirection())][item.getY()+getDy(item.getDirection())].getGameObject());
                }
			}
			else{
				item.setDelta(item.getDelta()+item.getSpeed());
				if(item.getDelta()>=1){
					moveGameObject(item);
					if(map.getSpaces()[item.getX()][item.getY()].getGameObject() != null){
                        bulletFoundTheObj(item, map.getSpaces()[item.getX()][item.getY()].getGameObject());
                    }
				}
			}
		}
		for (Bullet item: removeList){
		    map.getBullets().remove(item);
        }
	}

	private void bulletFoundTheObj(Bullet bullet, GameObject object){
	    if(object != null){
            removeList.add(bullet);
	        if(object instanceof Tank){
	            if(!((Tank)object).getTeam().equals(bullet.getTank().getTeam())){
                    ((Tank)object).setHp(((Tank)object).getTempHp() - bullet.getTank().getDamage());
                    if(((Tank)object).getTempHp() <= 0){
                        bullet.getTank().setSummOfDamage(bullet.getTank().getSummOfDamage() + bullet.getTank().getDamage() + ((Tank)object).getTempHp());
                        killTank((Tank)object);
                    }
                    else{
                        bullet.getTank().setSummOfDamage(bullet.getTank().getSummOfDamage() + bullet.getTank().getDamage());
                    }
                }
            }
            else if(object instanceof Wall){
	            map.getSpaces()[object.getX()][object.getY()].setGameObject(null);
            }
        }
    }

	public Map getGameMap() {
		return this.map;
	}

    public void killTank(Tank t){
	    map.addTankToOutList(t);
	    if(!t.getEnabled()){
	        map.getSpaces()[t.getX() + getDx(t.getDirection())][t.getY() + getDy(t.getDirection())].setGameObject(null);
        }
        map.getSpaces()[t.getX()][t.getY()].setGameObject(null);
	    map.getTanks().remove(t);
    }

	public GameObjectVM[][] getObjectsVM(){
	    GameObjectVM[][] answer = new GameObjectVM[map.getSpaces().length][map.getSpaces()[0].length];
	    for(int i = 0; i < answer.length; i++){
	        for(int j = 0; j< answer[0].length; j++){
	            if(map.getSpaces()[i][j].getGameObject()!= null && map.getSpaces()[i][j].getGameObject().getX() == i && map.getSpaces()[i][j].getGameObject().getY() == j) {
                    answer[i][j] = new GameObjectVM(map.getSpaces()[i][j].getGameObject());
                }
            }
        }
        return answer;
    }

    public MapGrounds[][] getMapGrounds(){
	    MapGrounds[][] answer = new MapGrounds[map.getSpaces().length][map.getSpaces()[0].length];
        for(int i = 0; i < answer.length; i++){
            for(int j = 0; j< answer[0].length; j++){
                answer[i][j] = map.getSpaces()[i][j].getMapground();
            }
        }
        return answer;
    }

    public void doCommand(Command cmd) throws Exception {
		if(cmd == null){
			throw new Exception();
		}
		if(cmd.getActivity() == Activities.Turn){
			Tank t =map.getTankById(cmd.getTankId().toString());
			if(t.getEnabled()){
				t.setDirection(cmd.getDirection());
			}
		}
		else if(cmd.getActivity() == Activities.Go){
			Tank t =map.getTankById(cmd.getTankId().toString());
			if(t.getEnabled()){
				if(map.canObjGoThere(t.getX(), t.getY(), cmd.getDirection())){
					int dx = getDx(cmd.getDirection());
					int dy = getDy(cmd.getDirection());
					map.getSpaces()[t.getX()+dx][t.getY() + dy].setGameObject(t);
					t.setDelta(0);
					t.setDirection(cmd.getDirection());
					t.setEnabled(false);
				}
			}
		}
		else {
            Tank item = map.getTankById(cmd.getTankId().toString());
            if (!item.getLastShoteTime().plusSeconds(item.getTimeToRecharge() / 1000).isAfter(LocalTime.now())) {
                item.setLastShoteTime(LocalTime.now());
                if (!(item.getX() + getDx(item.getDirection()) < 0 || item.getY() + getDy(item.getDirection()) < 0 ||
                        item.getX() + getDx(item.getDirection()) >= map.getSpaces().length ||
                        item.getY() + getDy(item.getDirection()) >= map.getSpaces()[0].length)) {
                    Bullet b = new Bullet(item, item.getX() + getDx(item.getDirection()), item.getY()+getDy(item.getDirection()));
                    map.getBullets().add(b);
                }
            }
        }
	}

	public void setCommand(int key, String  tankId){
	    if(map.getTankById(UUID.fromString(tankId).toString()) != null) {

            if (key == 65) { //key-code of A
                try {
                    if (map.getTankById(UUID.fromString(tankId).toString()).getDirection() != Directions.Left) {
                        doCommand(new Command(UUID.fromString(tankId), Activities.Turn, Directions.Left));
                    } else {
                        doCommand(new Command(UUID.fromString(tankId), Activities.Go, Directions.Left));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (key == 87) {//key-code of W
                try {
                    if (map.getTankById(UUID.fromString(tankId).toString()).getDirection() != Directions.Top) {
                        doCommand(new Command(UUID.fromString(tankId), Activities.Turn, Directions.Top));
                    } else {
                        doCommand(new Command(UUID.fromString(tankId), Activities.Go, Directions.Top));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (key == 68) {//key-code of D
                try {
                    if (map.getTankById(UUID.fromString(tankId).toString()).getDirection() != Directions.Right) {
                        doCommand(new Command(UUID.fromString(tankId), Activities.Turn, Directions.Right));
                    } else {
                        doCommand(new Command(UUID.fromString(tankId), Activities.Go, Directions.Right));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (key == 83) {//key-code of S
                try {
                    if (map.getTankById(UUID.fromString(tankId).toString()).getDirection() != Directions.Down) {
                        doCommand(new Command(UUID.fromString(tankId), Activities.Turn, Directions.Down));
                    } else {
                        doCommand(new Command(UUID.fromString(tankId), Activities.Go, Directions.Down));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (key == 32) {//key-code of Space
                try {
                    doCommand(new Command(UUID.fromString(tankId), Activities.Shoot, null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
	}

	private int getDx(Directions dir){
		if(dir == Directions.Top || dir == Directions.Down){
			return 0;
		}
		if(dir == Directions.Right){
			return 1;
		}
		return -1;
	}

	public Tank getTankByUserId(UUID userId){
	    for(Tank t: map.getTanks()){
	        if(t.getId().equals(userId)){
	            return t;
            }
        }
        return null;
    }

	private int getDy(Directions dir){
		if(dir == Directions.Right || dir == Directions.Left){
			return 0;
		}
		if(dir == Directions.Down){
			return 1;
		}
		return -1;
	}

	@Override
	void updateMap() {
		upDateBullets();
		updateTanks();
	}

	private void updateTanks(){
        for(Tank item : map.getTanks()){
            if(!item.getEnabled()){
                item.setDelta(item.getDelta()+item.getSpeed());
                if(item.getDelta()>=1){
                    moveGameObject(item);
                }
            }
        }
    }

	private void moveGameObject(GameObject obj){
		if(obj instanceof Tank) {
			map.getSpaces()[obj.getX()][obj.getY()].setGameObject(null);
		}
		if(obj.getDirection() == Directions.Top){
			try {
				obj.setY(obj.getY()-1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(obj.getDirection() == Directions.Right){
			try {
				obj.setX(obj.getX()+1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(obj.getDirection() == Directions.Down){
			try {
				obj.setY(obj.getY()+1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				obj.setX(obj.getX()-1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		obj.setDelta(0);
		obj.setEnabled(true);
	}

	@Override
	void loadMap(List<User> firstTeam, List<User> secondTeam) {
		this.map = MapMaker.makeMap(firstTeam, secondTeam);
		
	}


	public String getTankVMIdByUserId(String userId){
		for(Tank t: map.getTanks()){
			if(t.getId().toString().equals(userId)){
				return (new GameObjectVM(t)).getId();
			}
		}
		return null;
	}

	public boolean isThatTankInGame(String id) {
        for(Tank t: map.getTanks()){
            if(t.getId().toString().equals(id)){
                return true;
            }
        }
        return false;
	}
}
