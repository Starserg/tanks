package ru.eagle.tanks2d.controllers;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.eagle.tanks2d.BLL.BLL;
import ru.eagle.tanks2d.entities.GameRoom;
import ru.eagle.tanks2d.entities.GameRoomVM;
import ru.eagle.tanks2d.service.ContentManager;
import ru.eagle.tanks2d.tanksEntities.Bullet;
import ru.eagle.tanks2d.tanksEntities.GameObjectVM;
import ru.eagle.tanks2d.tanksEntities.Map;
import ru.eagle.tanks2d.tanksEntities.MapGrounds;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ServletController {


    @RequestMapping(value = "/getContent", method = RequestMethod.POST, produces = "application/json")
    public String getContent(String token, String pageName) {
        if(BLL.INSTANCE.checkToken(token)){
            return new Gson().toJson(ContentManager.getContent(pageName));
        }
        return new Gson().toJson("");
    }

    @RequestMapping(value = "/authorizationCheckServlet", method = RequestMethod.POST, produces = "application/json")
    public String authorization(@RequestParam String login, @RequestParam String password) {
        return new Gson().toJson(BLL.INSTANCE.checkUserData(login, password));
    }


    @RequestMapping(value = "/topListServlet", method = RequestMethod.POST, produces = "application/json")
    public String getTopList() {
        return new Gson().toJson(BLL.INSTANCE.getTopList());
    }


    @RequestMapping(value = "/achievementsListServlet", method = RequestMethod.POST, produces = "application/json")
    public String getAchList(@RequestParam String token) {
        return new Gson().toJson(BLL.INSTANCE.getAchList(token));
    }

    @RequestMapping(value = "/userStatisticServlet", method = RequestMethod.POST, produces = "application/json")
    public String getUserStatistic(@RequestParam String token) {
        return new Gson().toJson(BLL.INSTANCE.getUserStatisticByToken(token));
    }

    @RequestMapping(value = "/getEmail", method = RequestMethod.POST, produces = "application/json")
    public String getEmailByToken(@RequestParam String token) {
        return new Gson().toJson(BLL.INSTANCE.getEmailByToken(token));
    }

    @RequestMapping(value = "/getUserId", method = RequestMethod.POST, produces = "application/json")
    public String getUserId(@RequestParam String token, @RequestParam String masterLogin) {
        String userId = BLL.INSTANCE.getUserIdByToken(token);
        GameRoom room = BLL.INSTANCE.getGameRoomByMasterLogin(masterLogin);
        String answer = room.getGameLogic().getTankVMIdByUserId(userId);
        return new Gson().toJson(answer);
    }

    @RequestMapping(value = "/getGameRoomsListServlet", method = RequestMethod.POST, produces = "application/json")
    public String getGameRooms() {
        List<GameRoom> gameRooms = BLL.INSTANCE.getGameRooms();
        List<GameRoomVM> GRVMs = new ArrayList<>();
        for (GameRoom item: gameRooms) {
            GRVMs.add(item.getVM());
        }
        return new Gson().toJson(GRVMs);
    }

    @RequestMapping(value = "/createGameRoomServlet", method = RequestMethod.POST, produces = "application/json")
    public String createGameRoom(@RequestParam String token) {
        return new Gson().toJson(BLL.INSTANCE.addGameRoom(token));
    }

    @RequestMapping(value = "/getRoomServlet", method = RequestMethod.POST, produces = "application/json")
    public String getRoomByMasterLogin(@RequestParam String masterLogin) {
        GameRoom gameRoom = BLL.INSTANCE.getGameRoomByMasterLogin(masterLogin);
        if(gameRoom == null){
            return new Gson().toJson(false);
        }
        return new Gson().toJson(gameRoom.getVM());
    }

    @RequestMapping(value = "/getLoginByTokenServlet", method = RequestMethod.POST, produces = "application/json")
    public String getLoginByToken(@RequestParam String token) {
        return new Gson().toJson(BLL.INSTANCE.getUserLoginByToken(token));
    }

    @RequestMapping(value = "/takePlaceInTeam1", method = RequestMethod.POST, produces = "application/json")
    public String takePlaceIn1Team(@RequestParam  String masterLogin, @RequestParam String userToken) {
            return new Gson().toJson(BLL.INSTANCE.putUserTo1Team(userToken, masterLogin));
    }

    @RequestMapping(value = "/takePlaceInTeam2", method = RequestMethod.POST, produces = "application/json")
    public String takePlaceIn2Team(@RequestParam  String masterLogin, @RequestParam String userToken) {
            return new Gson().toJson(BLL.INSTANCE.putUserTo2Team(userToken, masterLogin));
    }

    @RequestMapping(value = "/IamHere", method = RequestMethod.POST, produces = "application/json")
    public String checkUser(@RequestParam String token, @RequestParam String masterLogin) {
        return new Gson().toJson(BLL.INSTANCE.checkUser(token, masterLogin));
    }

    @RequestMapping(value = "/startGame", method = RequestMethod.POST, produces = "application/json")
    public String startGame(@RequestParam  String token) {
        try {
            return new Gson().toJson(BLL.INSTANCE.startGame(token));
        }
        catch (Exception e){
            return new Gson().toJson(false);
        }
    }

    @RequestMapping(value = "/WaitGame", method = RequestMethod.POST, produces = "application/json")
    public String waitGame(@RequestParam  String masterLogin) {
        try {
            boolean isInGame = BLL.INSTANCE.getGameRoomByMasterLogin(masterLogin).isIngame();
            return new Gson().toJson(isInGame);
        }
        catch (Exception e){
            return new Gson().toJson(false);
        }
    }

    @RequestMapping(value = "/GetGameFrameGround", method = RequestMethod.POST, produces = "application/json")
    public String getGameGround(@RequestParam  String masterLogin, @RequestParam  String token) {
        MapGrounds[][] mg = BLL.INSTANCE.getGameRoomByMasterLogin(masterLogin).getGameLogic().getMapGrounds();
        return new Gson().toJson(mg);
    }

    @RequestMapping(value = "/getGameFrameObjects", method = RequestMethod.POST, produces = "application/json")
    public String getGameObjects(@RequestParam  String masterLogin, @RequestParam  String token) {
        try{
            GameObjectVM[][] objVM = BLL.INSTANCE.getGameRoomByMasterLogin(masterLogin).getGameLogic().getObjectsVM();
            return new Gson().toJson(objVM);
        }
        catch (Exception e){
            return new Gson().toJson(null);
        }
    }

    @RequestMapping(value = "/GameCommand", method = RequestMethod.POST, produces = "application/json")
    public String getGameCommand(@RequestParam  int key, @RequestParam  String masterLogin, @RequestParam  String token) {
        BLL.INSTANCE.getGameRoomByMasterLogin(masterLogin).getGameLogic().setCommand(key, BLL.INSTANCE.getUserIdByToken(token));
        return new Gson().toJson(true);
    }

    @RequestMapping(value = "/getToken", method = RequestMethod.POST, produces = "application/json")
    public String authorizeUser(@RequestParam String login, @RequestParam String password) {
        return new Gson().toJson(BLL.INSTANCE.authorization(login, password).getToken().getStringToken());
    }

    @RequestMapping(value = "/checkToken", method = RequestMethod.POST, produces = "application/json")
    public String checkToken(@RequestParam String token) {
        return new Gson().toJson(BLL.INSTANCE.checkToken(token));
    }

    @RequestMapping(value = "/exit", method = RequestMethod.POST, produces = "application/json")
    public String exit(@RequestParam String token){
        return new Gson().toJson(BLL.INSTANCE.exit(token));
    }

    @RequestMapping(value = "/iAmInTheBattle", method = RequestMethod.POST, produces = "application/json")
    public String iAmInTheBattle(@RequestParam String masterLogin, @RequestParam String token){
        String userId = BLL.INSTANCE.getUserIdByToken(token);
        return new Gson().toJson(BLL.INSTANCE.userInTheBattle(userId, masterLogin));
    }

    @RequestMapping(value = "/searchUserStatisticServlet", method = RequestMethod.POST, produces = "application/json")
    public String searchStatistic(@RequestParam String login){
        return new Gson().toJson(BLL.INSTANCE.getUserStatisticByLogin(login));
    }


    @RequestMapping(value = "/getGameBullets", method = RequestMethod.POST, produces = "application/json")
    public String getBullets(@RequestParam String masterLogin) {
        GameRoom room = BLL.INSTANCE.getGameRoomByMasterLogin(masterLogin);
        Map map = room.getGameLogic().getGameMap();
        List<Bullet> bullets = map.getBullets();
        return new Gson().toJson(bullets);
    }
}
