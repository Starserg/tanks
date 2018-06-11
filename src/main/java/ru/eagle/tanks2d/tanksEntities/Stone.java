package ru.eagle.tanks2d.tanksEntities;

import java.util.UUID;

public class Stone extends GameObject {

    public Stone(int x, int y){
        this.x = x;
        this.y = y;
        this.delta = 0;
        this.speed = 0;
        this.direction = Directions.Top;
        this.id = UUID.randomUUID();
    }
}
