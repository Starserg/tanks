package ru.eagle.tanks2d.entities;

import java.util.List;

public class GameRoomVM {

    public GameRoomVM(GameRoom gameRoom){
        this.master = gameRoom.getMaster();
        this.firstTeam = gameRoom.getFirstTeam();
        this.secondTeam = gameRoom.getSecondTeam();
        this.isInGame = gameRoom.isIngame();
    }

    private User master;
    private List<User> firstTeam;
    private List<User> secondTeam;
    private boolean isInGame;


    public List<User> getFirstTeam() {
        return firstTeam;
    }

    public List<User> getSecondTeam() {
        return secondTeam;
    }

    public String getMasterLogin(){
        return master.getLogin();
    }


}
