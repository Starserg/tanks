package ru.eagle.tanks2d.entities;

import java.util.UUID;

public class Achievement {

    public Achievement(UUID id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
        this.complited = false;
    }


    private UUID id;
    private String name;
    private String description;
    private boolean complited;

    public void setComplited(boolean complited){
        this.complited = complited;
    }

    public boolean isComplited() {
        return complited;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
