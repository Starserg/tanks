package ru.eagle.tanks2d.tanksEntities;

public class Space {

	public Space(MapGrounds m) {
		this.mapGround = m;
	}
	
	private GameObject gameObject;
	private MapGrounds mapGround;


	public MapGrounds getMapground() {
		return this.mapGround;
	}

	public GameObject getGameObject() {
		return this.gameObject;
	}
	
	public void setGameObject(GameObject obj){
		this.gameObject = obj;
	}


}
