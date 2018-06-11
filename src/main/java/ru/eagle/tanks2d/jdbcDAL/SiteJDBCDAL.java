package ru.eagle.tanks2d.jdbcDAL;


import ru.eagle.tanks2d.entities.Achievement;
import ru.eagle.tanks2d.entities.User;
import ru.eagle.tanks2d.entities.UserStatistic;
import ru.eagle.tanks2d.tanksEntities.Tank;
import ru.eagle.tanks2d.tanksEntities.TankBody;
import ru.eagle.tanks2d.tanksEntities.TankGun;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class SiteJDBCDAL {

private String baseBodyId ="44ff496c-34a1-4db9-a238-2cbd1b8f9dac";
private String baseGunId = "1cb4f3c4-b6fc-4b46-bdb3-e954064689e8";


    private  SiteJDBCDAL(){

        this.properties = new Properties();

        this.properties.setProperty("url", "jdbc:mariadb://localhost/tanks2d?useUnicode=yes&characterEncoding=UTF-8");
        this.properties.setProperty("jdbc.driver", "org.mariadb.jdbc.Driver");
        this.properties.setProperty("user", "root");
        this.properties.setProperty("password", "553829");
        try {
            Class.forName(this.properties.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found.");
            e.printStackTrace();
        }

    }

    private Properties properties;

    public static final SiteJDBCDAL INSTANCE = new SiteJDBCDAL(); // SINGLETONE

    public void addUserToBase(String login, String email, String password, String activatedCode){
        UUID userId = UUID.randomUUID();
        String SqlQuery = "INSERT INTO users (email, login, hash, id, id_Of_TankBody, id_Of_TankGun, activated, activCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = DriverManager.getConnection(this.properties.getProperty("url"), this.properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, email);
                st.setString(2, login);
                st.setString(3, this.makeHash(login, password));
                st.setString(4, userId.toString());
                st.setString(5, baseBodyId);
                st.setString(6, baseGunId);
                st.setString(7, "n");
                st.setString(8, activatedCode);
                st.executeQuery();

            }
            addUserStatisticToBase(userId);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void addUserStatisticToBase(UUID userId){
        String SqlQuery = "INSERT INTO statistic (userId, battles, wins, kills, mediumDamage) VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = DriverManager.getConnection(this.properties.getProperty("url"), this.properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, userId.toString());
                st.setString(2, "0");
                st.setString(3, "0");
                st.setString(4, "0");
                st.setString(5, "0");
                st.executeQuery();

            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String makeHash(String login, String password){
        String answer = null;
        try {
            String temp = login+password;
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(temp.getBytes());
            answer = new String(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public UserStatistic getUserStatisticById(String id){
        UUID uuidId = UUID.fromString(id);
        List<UserStatistic> allStats = getAllStatistics();
        for(UserStatistic u: allStats){
            if(u.getUserId().equals(uuidId)){
                return u;
            }
        }
        return null;
    }

    public User getUserById(String userId) throws Exception {
        List<User> allUsers = getAllUsers();
        for(User item: allUsers){
            if(item.getId().equals(userId)){
                return item;
            }
        }
        throw new Exception();
    }

    public boolean checkUserData(String login, String password){
        List<User> users = getAllUsers();
        String hash = makeHash(login, password);
        for(User item: users){
            if(item.getLogin().equals(login) && item.getHash().equals(hash)){
                return isUserActivated(item.getId());
            }
        }
        return false;
    }

    private boolean isUserActivated(String userId){
        String SqlQuery = "SELECT activated from users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, userId);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    String activated = "";
                    while (rs.next()){
                        activated = rs.getString(1);
                    }
                    return activated.equals("y");//y - "yes"; n - "no"
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return false;
        }
    }

    public List<UserStatistic> getTopUsersStatistic(){
        List<UserStatistic> allUserStats = getAllStatistics();
        List<UserStatistic> answer = new ArrayList<>();
        UserStatistic temp;
        for(int i = 0; i < 10; i++){
            if(allUserStats.size() == 0){
                break;
            }
            temp = allUserStats.get(0);
            for(int j = 0; j < allUserStats.size(); j++){
                if(allUserStats.get(j).getWinsStatistic()> temp.getWinsStatistic()){
                    temp = allUserStats.get(j);
                }
            }
            answer.add(temp);
            allUserStats.remove(temp);
        }
        return answer;
    }

    private List<User> getAllUsers() {
        List<User> users = null;

        String SqlQuery = "SELECT id, login, email, hash from users";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();

                try (ResultSet rs = st.getResultSet()) {
                    users = new ArrayList<>();
                    while (rs.next()) {
                        User item = new User(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(1));

                        users.add(item);
                    }

                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        return users;
    }

    public User getUser(String login, String password){
        List<User> all = getAllUsers();
        String hash = makeHash(login, password);
        for(User item: all){
            if(item.getLogin().equals(login) && item.getHash().equals(hash)){
                return item;
            }
        }
        return null;
    }

    private List<UserStatistic> getAllStatistics(){
        List<UserStatistic> stats = null;

        String SqlQuery = "SELECT userId, battles, wins, kills, mediumDamage from statistic";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();

                try (ResultSet rs = st.getResultSet()) {
                    stats = new ArrayList<>();
                    while (rs.next()) {
                        UserStatistic item = new UserStatistic(UUID.fromString(rs.getString(1)), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
                        stats.add(item);
                    }

                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        SqlQuery = "SELECT id, login from users";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();

                try (ResultSet rs = st.getResultSet()) {
                    String login;
                    UUID id;
                    while (rs.next()) {
                        id = UUID.fromString(rs.getString(1));
                        login = rs.getString(2);
                        for(UserStatistic item: stats){
                            if(item.getUserId().equals(id)){
                                item.setUserLogin(login);
                            }
                        }
                    }

                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }

        return stats;
    }

    public List<Achievement> getAchievements(String userId){
        List<Achievement> answer = null;

        String SqlQuery = "SELECT id, name, description from achievements";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();

                try (ResultSet rs = st.getResultSet()) {
                    answer = new ArrayList<>();
                    while (rs.next()) {
                        Achievement item = new Achievement(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3));
                        answer.add(item);
                    }

                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        setComplitedFlags(answer, userId);
        return answer;
    }

    private void setComplitedFlags(List<Achievement> achievements, String userId) {

        String SqlQuery = "SELECT userId, achId from userachs";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.executeQuery();

                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()) {
                        if(rs.getString(1).equals(userId)){
                            addComplitedFlag(achievements, rs.getString(2));
                        }
                    }
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
    }

    private void addComplitedFlag(List<Achievement> achievements, String achId){
        for(Achievement item : achievements){
            if(item.getId().toString().equals(achId)){
                item.setComplited(true);
            }
        }
    }

    public boolean loginContains(String login){
        List<User> users = getAllUsers();
        for(User item : users){
            if(item.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    public boolean emailContains(String email){
        List<User> users = getAllUsers();
        for(User item : users){
            if(item.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }

    public boolean changePassword(String code, String newPassword){
        if(!checkNewPassworCode(code)){
            return false;
        }
        String SqlQuery = "SELECT login from users WHERE newPasswordCode = ?";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, code);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    String login = "";
                    while (rs.next()){
                        login = rs.getString(1);
                    }
                    if(login.length() == 0){
                        return false;
                    }
                    if(setNewPasswordWhileChanging(login, newPassword)){
                        removeNewPassworCode(code);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return false;
        }
    }

    private void removeNewPassworCode(String code) {
        String SqlQuery = "UPDATE users  SET newPasswordCode = NULL WHERE newPasswordCode = ?";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, code);
                st.executeQuery();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
    }

    private boolean setNewPasswordWhileChanging(String login, String password){
        String SqlQuery = "UPDATE users  SET hash = ? WHERE login = ?";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, makeHash(login, password));
                st.setString(2, login);
                st.executeQuery();
                return true;
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean activateUserByActivationCode(String code) {
        try {
            UUID codeInUUID = UUID.fromString(code); // test that it is UUID
        }
        catch (Exception e){
            return false;
        }
        String SqlQuery = "SELECT activated from users WHERE activCode = ?";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, code);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    String activated = "";
                    while (rs.next()){
                        activated = rs.getString(1);
                    }
                    if(activated.length() == 0){
                        return false;
                    }
                    return setActivation(code);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return false;
        }
    }

    private boolean setActivation(String code){
        String SqlQuery = "UPDATE users  SET activated=\"y\" WHERE activCode = ?";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, code);
                st.executeQuery();
                return true;
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return false;
        }

    }

    private boolean setNewPassCode(String id, String newPasswordCode){
        String SqlQuery = "UPDATE users  SET newPasswordCode = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, newPasswordCode);
                st.setString(2, id);
                st.executeQuery();
                return true;
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean setNewPasswordCodeToUserByMail(String email, String newPasswordCode) {
        try {
            UUID codeInUUID = UUID.fromString(newPasswordCode); //test that it is UUID
        }
        catch (Exception e){
            return false;
        }
        String SqlQuery = "SELECT id from users WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, email);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    String id = "";
                    while (rs.next()){
                        id = rs.getString(1); // it's a test, if there is no user with that email, we return false
                    }
                    if(id.length() == 0){
                        return false;
                    }
                    return setNewPassCode(id, newPasswordCode);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkNewPassworCode(String code) {
        try{
            UUID codeInUUID = UUID.fromString(code); //test that it is UUID-code
        }
        catch (Exception e){
            return false;
        }

        String SqlQuery = "SELECT id from users WHERE newPasswordCode = ?";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, code);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    String id = "";
                    while (rs.next()){
                        id = rs.getString(1); // it's a test, if there is no user with that code, we return false
                    }
                    return !(id.length() == 0);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return false;
        }
    }

    public void updateStatistic(Tank tank, boolean isWin, UserStatistic userStatistic) {
        if(isWin){
            userStatistic.setWinsStatistic((userStatistic.getWinsStatistic()*userStatistic.getBattles()+1)/(userStatistic.getBattles()+1));
        }
        else{
            userStatistic.setWinsStatistic((userStatistic.getWinsStatistic()*userStatistic.getBattles())/(userStatistic.getBattles()+1));
        }
        userStatistic.addKills(tank.getKills());
        userStatistic.setMediumDamage((userStatistic.getMediumDamage()*userStatistic.getBattles() + tank.getDamage())/(userStatistic.getBattles()+1));
        userStatistic.addBattle();
        String SqlQuery = "UPDATE statistic  SET battles = ?, wins = ?, kills = ?, mediumDamage = ? WHERE userId = ?";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, "" + userStatistic.getBattles());
                st.setString(2, "" + (userStatistic.getWinsStatistic()*userStatistic.getBattles()));
                st.setString(3, "" + userStatistic.getKills());
                st.setString(4, "" + userStatistic.getMediumDamage());
                st.setString(5, userStatistic.getUserId().toString());
                st.executeQuery();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
        updateAchievements(userStatistic);
    }

    private void updateAchievements(UserStatistic stat){
        List<Achievement> achievements = getAchievements(stat.getUserId().toString());
        for(Achievement item: achievements){
            if(!item.isComplited()){
                checkAchievement(item, stat);
                if(item.isComplited()){
                    saveAchToBase(item, stat.getUserId().toString());
                }
            }
        }

    }

    private void saveAchToBase(Achievement ach, String userId){
        String SqlQuery = "INSERT INTO userachs (userId, achId) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, userId);
                st.setString(2, ach.getId().toString());
                st.executeQuery();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
    }

    private void checkAchievement(Achievement ach, UserStatistic stat) {

        String SqlQuery = "SELECT type, count from achievements WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, ach.getId().toString());
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    String type = "";
                    int count = 999; //very big number for initialisation
                    while (rs.next()) {
                        type = rs.getString(1);
                        count = rs.getInt(2);
                    }
                    if(type.equals("battles")){
                        if(stat.getBattles()>= count){
                            ach.setComplited(true);
                        }
                    }
                    else if(type.equals("kills")){
                        if(stat.getKills()>= count){
                            ach.setComplited(true);
                        }
                    }
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
        }
    }

    public UserStatistic getUserStatisticByLogin(String login) {
        List<UserStatistic> allStats = getAllStatistics();
        for(UserStatistic item: allStats){
            if(item.getUserLogin().equals(login)){
                return item;
            }
        }
        return null;
    }

    public String getEmailById(String id) {
        String SqlQuery = "SELECT email from users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, id);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    String email = "";
                    while (rs.next()){
                        email = rs.getString(1);
                    }
                    return email;
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return null;
        }
    }

    public TankBody getTankBodyOfUser(String id) {
        String bodyId = getIdOfBodyBuUserId(id);
        String SqlQuery = "SELECT hp, speed from tankbodies WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, bodyId);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()){
                        return new TankBody(rs.getInt(1), rs.getDouble(2));
                    }
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return null;
        }
        return  null;
    }

    private String getIdOfBodyBuUserId(String userId){
        String bodyId = "";
        String SqlQuery = "SELECT id_Of_TankBody from users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {
            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, userId);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()){
                        bodyId = rs.getString(1);
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return null;
        }
        return bodyId;
    }

    public TankGun getTankGunOfUser(String id) {
        String gunId = getIdOfGunByUserId(id);

        String SqlQuery = "SELECT damage, time_To_Recharge from tankguns WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, gunId);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()){
                        return new TankGun(rs.getInt(1), rs.getInt(2));
                    }
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return null;
        }
        return  null;
    }

    private String getIdOfGunByUserId(String userId){
        String gunId = "";
        String SqlQuery = "SELECT id_Of_TankGun from users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties)) {

            try (PreparedStatement st = connection.prepareStatement(SqlQuery)) {
                st.setString(1, userId);
                st.executeQuery();
                try (ResultSet rs = st.getResultSet()) {
                    while (rs.next()){
                        gunId = rs.getString(1);
                    }
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Connection problem.");
            e.printStackTrace();
            return null;
        }
        return gunId;
    }
}
