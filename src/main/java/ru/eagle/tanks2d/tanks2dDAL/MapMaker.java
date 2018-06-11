package ru.eagle.tanks2d.tanks2dDAL;

import ru.eagle.tanks2d.entities.User;
import ru.eagle.tanks2d.jdbcDAL.SiteJDBCDAL;
import ru.eagle.tanks2d.tanksEntities.*;

import java.time.LocalTime;
import java.util.List;

public class MapMaker {

    public static Map makeMap(List<User> firsTeam, List<User> secondTeam){
        Space[][] spaces = getSpaces();
        for(int j = 0; j < spaces.length; j++) {
            for (int i = 0; i < spaces[j].length; i++) {
                if(map[j][i] == 'w'){
                    spaces[i][j].setGameObject(new Wall(i, j));
                }
                else if(map[j][i] == 's'){
                    spaces[i][j].setGameObject(new Stone(i, j));
                }
            }
        }
        String userId;
        Tank t;
        for(int i = 0; i < firsTeam.size(); i++){
            userId = firsTeam.get(i).getId();
            TankBody body = SiteJDBCDAL.INSTANCE.getTankBodyOfUser(userId);
            TankGun gun = SiteJDBCDAL.INSTANCE.getTankGunOfUser(userId);
            System.out.println(body + "    " + gun);
            t = new Tank(userId, body, gun, Teams.First, i, spaces.length-1);
            spaces[i][spaces.length-1].setGameObject(t);
        }
        for(int i = 0; i < secondTeam.size(); i++){
            userId = secondTeam.get(i).getId();
            TankBody body = SiteJDBCDAL.INSTANCE.getTankBodyOfUser(userId);
            TankGun gun = SiteJDBCDAL.INSTANCE.getTankGunOfUser(userId);
            System.out.println(body + "    " + gun);
            t = new Tank(userId,body, gun, Teams.Second, i, 0);
            spaces[i][0].setGameObject(t);
        }
        Map answer = new Map(spaces);
        return answer;
    }


    private static Space[][] getSpaces(){
        Space[][] answer = new Space[map.length][map[0].length];
        for(int j = 0; j < answer.length; j++) {
            for (int i = 0; i < answer[j].length; i++) {
                if(map[j][i] == 'g'){
                    answer[i][j] = new Space(MapGrounds.Grass);
                }
                else{
                    answer[i][j] = new Space(MapGrounds.None);
                }
            }
        }
        return answer;
    }


    private static char[][] map = {
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'},
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'},
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'w', 'g', 'g', 'g', 'w', 'n', 'n', 'n', 'w', 'n', 'w', 'w', 'n'},
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 's', 'g', 's', 'w', 'w', 'w', 'n', 'n', 'n', 'n', 'w', 'n'},
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 's', 's', 's', 's', 's', 's', 's', 'n', 'w', 'n', 'w', 'n'},
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 's', 's', 's', 'n', 'n', 'n', 'n', 'n', 'w', 'w', 'w', 'n'},
            {'g', 'g', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'g', 'g', 'n', 'n', 'g', 'n', 'n', 'g', 'g', 'g', 'n'},
            {'g', 'g', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'g', 's', 'g', 'n', 'g', 'n', 'n', 'g', 'w', 'n', 'n'},
            {'w', 'w', 'n', 'n', 'n', 'n', 'n', 'w', 'n', 'g', 'g', 'g', 'w', 'g', 'w', 'n', 'g', 'w', 'w', 'n'},
            {'w', 'w', 'n', 'n', 'n', 'n', 'n', 'w', 'n', 'g', 'g', 'n', 'g', 'g', 'w', 'w', 'g', 'w', 'n', 'n'},
            {'w', 'w', 'n', 'n', 'n', 'n', 'n', 'w', 'n', 'n', 'n', 'n', 'w', 'g', 'w', 'n', 'n', 'w', 'w', 'n'},
            {'w', 'w', 'g', 's', 's', 's', 'n', 'w', 'n', 'n', 'n', 'n', 'w', 'n', 'w', 'n', 'n', 'n', 'n', 'n'},
            {'w', 'w', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'n', 's', 's', 'n', 'w', 'n', 'n', 'n', 'n', 'n'},
            {'w', 'w', 'g', 's', 's', 's', 'n', 'w', 'n', 'g', 'g', 's', 's', 'n', 'w', 'n', 'n', 'n', 'n', 'n'},
            {'w', 'w', 'g', 'n', 'n', 'n', 'n', 'w', 'w', 'w', 'g', 'w', 'w', 'w', 'w', 'n', 'n', 'n', 'n', 'n'},
            {'n', 'n', 'g', 'n', 'n', 'n', 'n', 'w', 'n', 'n', 'n', 'n', 'n', 'n', 'w', 'n', 'n', 'n', 'n', 'n'},
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'w', 'n', 'n', 'n', 'n', 'n', 'n', 'w', 'n', 'n', 'n', 'n', 'n'},
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'w', 'n', 'n', 'n', 'n', 'n', 'n', 'w', 'n', 'n', 'n', 'n', 'n'},
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'},
            {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'}

    };
}
