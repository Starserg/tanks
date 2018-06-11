package ru.eagle.tanks2d.service;


public class ContentManager {

    public static String getContent(String pageName){
        if(pageName.equals("toplist")){
            return statisticContent;
        }
        if(pageName.equals("gameroom")){
            return gameRoomContent;
        }
        if(pageName.equals("progress")){
            return progressContent;
        }
        if(pageName.equals("personalaccount")) {
            return personalAccountContent;
        }
        return "";
    }


    private static String gameRoomContent ="<button id = \"newgameroom\">Создать комнату</button>" +
            "<table id = \"gameroomstable\">" +
            " <thead><tr>" +
            "<td>Номер игровой комнаты</td>" +
            "<td>Ник администратора комнаты</td>" +
            "<td>Действие</td>" +
            "</tr>" +
            "</thead>" +
            " <tbody id = \"gameroomstablebody\"></tbody>" +
            "</table>" +
            "<button id = \"backbutton\">Назад к списку комнат</button>" +
            "<button id = \"startGameButton\">Начать игру</button>" +
            "<table id = \"teamstable1\" class = \"hiddenteamstable\">" +
            "<thead>" +
            "<tr>" +
            "<td>Первая команда</td>" +
            "<td>Действие</td>" +
            " </tr>" +
            "</thead>" +
            " <tbody id = \"teamstable1body\"></tbody>" +
            "</table>" +
            "<table id = \"teamstable2\" class = \"hiddenteamstable\">" +
            " <thead>" +
            "<tr>" +
            " <td>Вторая команда</td>" +
            "<td>Действие</td>" +
            "</tr>" +
            "</thead>" +
            "<tbody id = \"teamstable2body\"></tbody>" +
            "</table>" + "<div id = \"bulletContainer\"></div>";


    private static String progressContent ="<div class = \"contentfieldprogress\">" +
            "<p class = \"progress\">Достижения</p><br>" +
            "<table class=\"sort\" align=\"center\">" +
            "<thead>" +
            " <tr>" +
            "<td>Название достижения</td>" +
            " <td>Условие получение</td>" +
            "<td>Выполнение</td>" +
            "</tr>" +
            "</thead>" +
            "<tbody id = \"achievementList\">" +
            "</tbody>" +
            " </table>" +
            "</div>";


    private static String statisticContent = "<p class = \"top\" >Достижения</p><br>\n" +
            "<div id = \"contentfieldtop\" class = \"contentfieldtop1\"  >" +
            "<table class=\"sort\" align=\"center\">" +
            "<thead>" +
            "<tr><td>Место</td>" +
            "<td>Ник игрока</td>" +
            "<td>Процент побед %</td>" +
            "</tr>" +
            "</thead>" +
            "<tbody>" +
            "<tr>" +
            "<td>1</td>" +
            "<td id = \"login1\"></td>" +
            "<td id = \"stat1\"></td>" +
            "</tr>" +
            "<tr>" +
            "<td>2</td>" +
            "<td id = \"login2\"></td>" +
            "<td id = \"stat2\"></td>" +
            "</tr>" +
            "<tr>" +
            "<td>3</td>" +
            "<td id = \"login3\"></td>" +
            "<td id = \"stat3\"></td>" +
            "</tr><tr><td>4</td>" +
            "<td id = \"login4\"></td>" +
            "<td id = \"stat4\"></td>" +
            "</tr>" +
            "<tr>" +
            "<td>5</td>" +
            "<td id = \"login5\"></td>" +
            "<td id = \"stat5\"></td>" +
            "</tr>" +
            "<tr>" +
            "<td>6</td>" +
            "<td id = \"login6\"></td>" +
            "<td id = \"stat6\"></td>" +
            "</tr>" +
            "<tr>" +
            "<td>7</td>" +
            "<td id = \"login7\"></td>" +
            "<td id = \"stat7\"></td>" +
            "</tr>" +
            "<tr>" +
            "<td>8</td>" +
            "<td id = \"login8\"></td>" +
            "<td id = \"stat8\"></td>" +
            "</tr>" +
            "<tr>" +
            "<td>9</td>" +
            "<td id = \"login9\"></td>" +
            " <td id = \"stat9\"></td>" +
            "</tr>" +
            "<tr><td>10</td>" +
            "<td id = \"login10\"></td>" +
            "<td id = \"stat10\"></td>" +
            "</tr>" +
            "</tbody>" +
            " </table>"+
            "</div>" +
            "<input type = \"button\" class = \"buttonlogintop\" id = \"buttonTransform\" value= \"найти статистику игрока\">" +
            " <div class = \"contentfieldtop2\" id = \"contentfield\" style=\"display:none\">" +
            "<form>" +
            "<p style=\"text-indent: 25%\"> <label for=\"search\"><font class = \"search\" color = \"white\">Ник:<input type = \"text\" class = \"search\" id = \"search\" name=\"search\" min=\"1\" max=\"30\" placeholder=\"Введите ник игрока\">" +
            "<input type = \"button\" class = \"button\" id = \"buttonSearch\" value=\"Найти\"></p>" +
            "</form>" +
            "<p class = \"personalaccount\">Статистика игрока</p><br>" +
            "<p class=\"personalaccountelement\" id=\"loginField\">Ник:</p>" +
            "<p class=\"personalaccountelement\" id=\"winsField\">Процент побед:</p>" +
            "<p class=\"personalaccountelement\" id=\"battlesField\">Количество боев:</p>" +
            "<p class=\"personalaccountelement\"  id=\"killsField\">Уничтожено танков:</p>" +
            "</div>";


    private static String personalAccountContent = "<div class = \"contentfield\">" +
            "<p class = \"personalaccount\">Личный кабинет</p><br>" +
            "<p class=\"personalaccountelement\" id = \"userLogin\">Ник:</p>" +
            "<p class=\"personalaccountelement\" id = \"userEmail\">Email:</p>" +
            "<p class=\"personalaccountelement\" id = \"percentOfWins\">Процент побед:</p>" +
            "<p class=\"personalaccountelement\" id = \"battles\">Количество боев:</p>" +
            "<p class=\"personalaccountelement\" id = \"kills\">Уничтожено танков:</p><br>" +
            "<form>" +
            "<input type = \"button\" id = \"buttonexit\" class = \"buttonexit\" value= \"Выйти\"><br>" +
            "</form>" +
            "</div>";

}
