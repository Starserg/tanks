package ru.eagle.tanks2d.tanksEntities;

public class GameObjectVM {

    public GameObjectVM(GameObject gameObject){
        this.direction = getIntDirection(gameObject);
        this.x = gameObject.getX();
        this.y = gameObject.getY();
        this.delta = gameObject.getDelta();
        setDescription(gameObject);
        id = description + gameObject.getId().toString().substring(1, 5);
    }

    private int x;
    private int y;
    private double delta;
    private String id;

    private String description;

    private int direction;

    public String getId(){
        return id;
    }

    private int getIntDirection(GameObject gameObject){
        Directions direction = gameObject.getDirection();
        if(direction == Directions.Top){
            return 1;
        }
        if(direction == Directions.Right){
            return 2;
        }
        if(direction == Directions.Down){
            return 3;
        }
        return 4;
    }

    private void setDescription(GameObject gameObject){
        if(gameObject instanceof Tank ){
            if(((Tank)gameObject).getTeam() == Teams.First){
                description = "tank1";
            }
            else{
                description = "tank2";
            }
        }
        else if(gameObject instanceof Wall){
                description = "wall";
            }
        else if(gameObject instanceof Stone){
            description = "stone";
        }
        else if(gameObject instanceof Bullet){
            description = "bullet";
        }
    }
}
