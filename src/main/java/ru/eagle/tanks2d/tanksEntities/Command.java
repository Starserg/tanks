package ru.eagle.tanks2d.tanksEntities;

import java.util.UUID;

public class Command {

    public Command(UUID tankId, Activities act, Directions dir){
        this.tankId = tankId;
        this.activity = act;
        this.direction = dir;
    }

    private UUID tankId;

    private Activities activity;

    private Directions direction;

    public UUID getTankId(){
        return this.tankId;
    }

    public Activities getActivity() {
        return this.activity;
    }

    public Directions getDirection() {
        return this.direction;
    }
}
