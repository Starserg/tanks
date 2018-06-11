package ru.eagle.tanks2d.BLL;



import ru.eagle.tanks2d.entities.*;
import ru.eagle.tanks2d.jdbcDAL.SiteJDBCDAL;
import ru.eagle.tanks2d.tanksEntities.Tank;
import ru.eagle.tanks2d.tanksEntities.Teams;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BLL {

    private BLL(){
    }

    public static final BLL INSTANCE = new BLL();   // SINGLETONE

    private HashMap<Object, Token> userToken = new HashMap<>(); //Key(Object) is ID of User

    private List<GameRoom> gameRooms = new ArrayList<GameRoom>();

    public GameRoom getGameRoomByMasterLogin(String masterLogin){
        for(GameRoom item: gameRooms){
            if(item.getMasterLogin().equals(masterLogin)){
                return item;
            }
        }
        return null;
    }

    public boolean addGameRoom(String masterToken){
        try{
            User master = SiteJDBCDAL.INSTANCE.getUserById(getUserIdByToken(masterToken));
            GameRoom newGameRoom = new GameRoom(master);
            gameRooms.add(newGameRoom);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<GameRoom> getGameRooms() {
        return gameRooms;
    }

    public String getUserIdByToken(String token){
        for(Object tempId : userToken.keySet()){
            if(userToken.get(tempId).getStringToken().equals(token)){
                return (String)tempId;
            }
        }
        return null;
    }

    public HashMap<Object, Token> getUserToken() {
        return userToken;
    }

    public boolean checkToken(String token) {
        if (token == null) {
            return false;
        }

        for (Token tkn : userToken.values()) {
            if (token.equals(tkn.getStringToken())) {
                return true;
            }
        }
        return false;
    }

    public boolean removeToken(Token token) {
        if (userToken.containsValue(token)) {
            for (Object key : userToken.keySet()) {
                if (userToken.get(key) == token) {
                    userToken.remove(key);
                    return true;
                }
            }
        }
        return false;
    }

    public UserWithToken authorization(String login, String password) {
        User u = SiteJDBCDAL.INSTANCE.getUser(login, password);
        String removeTokenId = "";
        for(Object tempId : userToken.keySet()){
            if(tempId.toString().equals(u.getId())){
               removeTokenId = tempId.toString();
               break;
            }
        }
        if(removeTokenId.length() > 0){
            removeToken(userToken.get(removeTokenId));
        }
        UserWithToken userWithToken = new UserWithToken(u);
        userToken.put(userWithToken.getId(), userWithToken.getToken());
        return userWithToken;
    }

    public boolean isUserDataValid(String email, String login, String password){
        if(login.contains("@")|| password.contains("@")){
            return false;
        }
        return (isStringValid(login) && isPasswordValid(password) && isEmailValid(email));
    }

    public boolean isLoginFree(String login){
        return !SiteJDBCDAL.INSTANCE.loginContains(login);
    }

    public boolean isEmailFree(String email){
        return !SiteJDBCDAL.INSTANCE.emailContains(email);
    }

    public boolean isStringValid(String s){
        boolean hasLetter = false;
        if(s == null || s.length() == 0){
            return false;
        }
        if(s.replaceAll("_", "").replaceAll("@", "").length() == 0){
            return false;
        }
        char[] arr = s.toCharArray();
        for(int i = 0; i < s.length(); i++){
            if(arr[i] >= 'a' && arr[i] <= 'z'){
                hasLetter = true;
                continue;
            }
            if(arr[i] >= 'A' && arr[i] <= 'Z'){
                hasLetter = true;
                continue;
            }
            if(arr[i] >= '0' && arr[i] <= '9'){
                continue;
            }
            if(arr[i] == '@' || arr[i] == '_'){
                continue;
            }
            return false;
        }
        if(arr[0] >= '0' && arr[0] <= '9'){
            return false;
        }
        return hasLetter;
    }

    public boolean isPasswordValid(String s){
        return (s!= null && isStringValid("a"+s) && s.length()>0); // first simbol of password can be not letter
    }

    public boolean isEmailValid(String s){
        return isStringValid(s.replace("@", "").replace(".", ""));
    }

    public void addUser(String email, String login, String password, String activatedCode) {
        SiteJDBCDAL.INSTANCE.addUserToBase(login, email, password, activatedCode);
    }

    public boolean startGame(String token){
        try {
            String masterId = getUserIdByToken(token);
            String masterLogin = SiteJDBCDAL.INSTANCE.getUserById(masterId).getLogin();
            GameRoom room = getGameRoomByMasterLogin(masterLogin);
            if(room.getFirstTeam().size() > 0 && room.getSecondTeam().size()>0){
                room.startGame();
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean checkUser(String token, String masterLogin){
        try {
            String userId = getUserIdByToken(token);
            User user;
            if(userId!= null) {
                user = SiteJDBCDAL.INSTANCE.getUserById(userId);
            }
            else{
                return false;
            }
            GameRoom gameRoom = getGameRoomByMasterLogin(masterLogin);
            if(gameRoom != null) {
                gameRoom.addCheckedUser(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean putUserTo2Team(String userToken, String masterLogin){
        String userId = getUserIdByToken(userToken);
        User u;
        try {
            u = SiteJDBCDAL.INSTANCE.getUserById(userId);
            GameRoom gameRoom = getGameRoomByMasterLogin(masterLogin);
            gameRoom.addUserToSecondTeam(u);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean putUserTo1Team(String userToken, String masterLogin){
        String userId = getUserIdByToken(userToken);
        User u;
        try {
            u = SiteJDBCDAL.INSTANCE.getUserById(userId);
            GameRoom gameRoom = getGameRoomByMasterLogin(masterLogin);
            gameRoom.addUserToFirstTeam(u);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getUserLoginByToken(String token){
        try {
            String userId = getUserIdByToken(token);
            User user = SiteJDBCDAL.INSTANCE.getUserById(userId);
            return user.getLogin();
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public UserStatistic getUserStatisticByToken(String token) {
        String userId = getUserIdByToken(token);
        return SiteJDBCDAL.INSTANCE.getUserStatisticById(userId);
    }

    public List<Achievement> getAchList(String token) {
        String userId = getUserIdByToken(token);
        return SiteJDBCDAL.INSTANCE.getAchievements(userId);
    }

    public List<UserStatistic> getTopList() {
        return SiteJDBCDAL.INSTANCE.getTopUsersStatistic();
    }

    public boolean checkUserData(String login, String password) {
        return SiteJDBCDAL.INSTANCE.checkUserData(login, password);
    }

    public boolean activate(String code) {
        try{
            return SiteJDBCDAL.INSTANCE.activateUserByActivationCode(code);
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean setNewPasswordCodeToUser(String email, String newPasswordCode) {
        return SiteJDBCDAL.INSTANCE.setNewPasswordCodeToUserByMail(email, newPasswordCode);
    }

    public boolean checkNewPassworCode(String code) {
        return SiteJDBCDAL.INSTANCE.checkNewPassworCode(code);
    }

    public boolean replacePassword(String code, String newpassword) {
        return SiteJDBCDAL.INSTANCE.changePassword(code, newpassword);
    }

    public boolean exit(String token) {
        String id = getUserIdByToken(token);
        return removeToken(userToken.get(id));
    }

    public void endGameInRoom(GameRoom gameRoom) {
        if(gameRoom.getFirstTeam().size() > 0 && gameRoom.getSecondTeam().size() == 0){
            for(Tank t : gameRoom.getGameLogic().getGameMap().getTanksOutOfGame()){
                if(t.getTeam() == Teams.First){
                    SiteJDBCDAL.INSTANCE.updateStatistic(t,true, SiteJDBCDAL.INSTANCE.getUserStatisticById(t.getId().toString()));
                }
                else{
                    SiteJDBCDAL.INSTANCE.updateStatistic(t,false, SiteJDBCDAL.INSTANCE.getUserStatisticById(t.getId().toString()));
                }
            }
        }
        else if(gameRoom.getSecondTeam().size() > 0 && gameRoom.getFirstTeam().size() == 0){
            for(Tank t : gameRoom.getGameLogic().getGameMap().getTanksOutOfGame()){
                if(t.getTeam() == Teams.Second){
                    SiteJDBCDAL.INSTANCE.updateStatistic(t,true, SiteJDBCDAL.INSTANCE.getUserStatisticById(t.getId().toString()));
                }
                else{
                    SiteJDBCDAL.INSTANCE.updateStatistic(t,false, SiteJDBCDAL.INSTANCE.getUserStatisticById(t.getId().toString()));
                }
            }
        }
        else{
            for(Tank t : gameRoom.getGameLogic().getGameMap().getTanksOutOfGame()) {
                SiteJDBCDAL.INSTANCE.updateStatistic(t,false, SiteJDBCDAL.INSTANCE.getUserStatisticById(t.getId().toString()));
            }
        }
        gameRooms.remove(gameRoom);
    }

    public boolean userInTheBattle(String userId, String roomMasterLogin) {
        try {
            User user;
            if(userId!= null) {
                user = SiteJDBCDAL.INSTANCE.getUserById(userId);
            }
            else{
                return false;
            }
            GameRoom gameRoom = getGameRoomByMasterLogin(roomMasterLogin);
            if(gameRoom != null) {
                gameRoom.addCheckedUserInTheBattle(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getEmailByToken(String token) {
        String id = getUserIdByToken(token);
        return SiteJDBCDAL.INSTANCE.getEmailById(id);
    }

    public UserStatistic getUserStatisticByLogin(String login) {
        return SiteJDBCDAL.INSTANCE.getUserStatisticByLogin(login);
    }
}
