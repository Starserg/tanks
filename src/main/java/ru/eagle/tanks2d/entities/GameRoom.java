package ru.eagle.tanks2d.entities;

import ru.eagle.tanks2d.BLL.BLL;
import ru.eagle.tanks2d.tanks2dBLL.GameLogic;
import ru.eagle.tanks2d.tanksEntities.Tank;

import java.util.*;

public class GameRoom {

    public GameRoom(User master){
        this.master = master;
        endGameTimer = new Timer();
        inGame = false;
        firstTeam = new ArrayList<User>();
        firstTeam.add(this.master);
        secondTeam = new ArrayList<User>();
        checkedUsers = new ArrayList<>();
        checkTimer = new Timer();
        checkTimerBattle = new Timer();
        checkTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkUsers();
            }
        }, checkTimerTime/2, checkTimerTime/2);
    }


    private int minutesToBattle = 5; //constants
    private int timeToUpdate = 40;
    private int checkTimerTime = 1000;

    private GameLogic gameLogic;
    private Timer endGameTimer;
    private boolean inGame;
    private User master;
    private List<User> firstTeam;
    private List<User> secondTeam;
    private List<User> checkedUsers;
    private Timer checkTimer;
    private Timer checkTimerBattle;


    public void startGame(){
        stopCheckTimer();
        endGameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                endGame();
            }
        }, minutesToBattle*60*1000); // 1 minute = 60 seconds; 1 second = 1000 milliseconds
        this.inGame = true;
        this.gameLogic = new GameLogic(timeToUpdate, firstTeam, secondTeam);
        this.checkTimerBattle.schedule(new TimerTask() {
            @Override
            public void run() {
                checkUsersBattle();
            }
        }, checkTimerTime, checkTimerTime);
    }

    public boolean isIngame() {
        return inGame;
    }

    public User getMaster(){
        return this.master;
    }

    public List<User> getFirstTeam() {
        return firstTeam;
    }

    public List<User> getSecondTeam() {
        return secondTeam;
    }

    public void addUserToFirstTeam(User u){
        firstTeam.add(u);
    }

    public void addUserToSecondTeam(User u){
        secondTeam.add(u);
    }

    public String getMasterLogin(){
        return master.getLogin();
    }

    public void addCheckedUser(User user){
        this.checkedUsers.add(user);
    }

    private void checkUsers(){
        checkUsersInTheTeam(firstTeam, checkedUsers);
        checkUsersInTheTeam(secondTeam, checkedUsers);
        checkedUsers.clear();
        boolean masterhere = false;
        for(User item: firstTeam){
            if(item.getLogin().equals(master.getLogin())){
                masterhere = true;
            }
        }
        if(!masterhere){
            BLL.INSTANCE.getGameRooms().remove(this);
        }
    }

    private void checkUsersBattle(){
        checkUsersInTheTeamBattle(firstTeam, checkedUsers);
        checkUsersInTheTeamBattle(secondTeam, checkedUsers);
        checkedUsers.clear();
        if(firstTeam.size() == 0 || secondTeam.size() == 0){
            endGame();
        }
    }

    private void endGame() {
        checkTimerBattle.cancel();
        for(int i = 0; i < gameLogic.getGameMap().getTanks().size(); i++){
            gameLogic.killTank(gameLogic.getGameMap().getTanks().get(i));
        }
        BLL.INSTANCE.endGameInRoom(this);
    }

    public GameRoomVM getVM(){
        return new GameRoomVM(this);
    }


    private void checkUsersInTheTeamBattle(List<User> team, List<User> checked){
        boolean success;
        for(int i = 0; i < team.size(); i++){
            success = false;
            for(int j = 0; j < checked.size(); j++){
                if(checked.get(j).getLogin().equals(team.get(i).getLogin()) && gameLogic.isThatTankInGame(team.get(i).getId())){
                    success = true;
                    checked.remove(checked.get(j));
                    break;
                }
            }
            if(!success){
                try{
                    Tank t = gameLogic.getGameMap().getTankById(team.get(i).getId());
                    if(t!= null){
                        t.setHp(0);
                        gameLogic.killTank(t);
                    }
                }
               catch (Exception e){
                    e.printStackTrace();
               }
                team.remove(team.get(i));
            }
        }
    }

    private void checkUsersInTheTeam(List<User> team, List<User> checked){
        boolean success;
        for(int i = 0; i < team.size(); i++){
            success = false;
            for(int j = 0; j < checked.size(); j++){
                if(checked.get(j).getLogin().equals(team.get(i).getLogin())){
                    success = true;
                    checked.remove(checked.get(j));
                    break;
                }
            }
            if(!success){
                team.remove(team.get(i));
            }
        }
    }

    public void stopCheckTimer(){
        checkTimer.cancel();
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void addCheckedUserInTheBattle(User user) {
        checkedUsers.add(user);
    }
}
