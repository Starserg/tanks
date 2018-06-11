var maxCountOfPlayersInTeam = 5;
var roomListIntervalTime = 1000;        //constants
var gameRoomIntervalTime = 1000;
var iAmHereIntervalTime = 250;
var waitingGameIntervalTime = 200;
var inTheBattleIntervalTime = 250;
var updateGamePictureIntervalTime = 40; //for 25 fps
var globalGameroomsList;
var gameRoomIntervalId;
var roomMasterLogin;
var roomListIntervalId;
var iAmHereIntervalId;
var waitingGameIntervalId;
var inTheBattleIntervalId;
var updateGamePictureIntervalId;
var userCanTakePartInTeam = true;
var userIsMaster = false;
var inGame = false;
var iAmMaster = false;
var myGameId;

$(document).bind('afterready', function() {
        var token = localStorage.getItem("token");
        setHandler();
        document.querySelector("#newgameroom").onclick = function () {
            createNewGameroom(token);
        };
        document.querySelector("#backbutton").onclick = function () {
            goBackToList();
        };
        ajaxGetListOfGameRooms();
        roomListIntervalId = setInterval(ajaxGetListOfGameRooms, roomListIntervalTime);
    }
);

function getMyGameId() {
    $.ajax({
        url: "/getUserId",
        data: {token: localStorage.getItem("token"), masterLogin: roomMasterLogin},
        method: "POST",
        success: function (request) {
           myGameId = request;
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function ajaxGetListOfGameRooms(){
    $.ajax({
        url: "/getGameRoomsListServlet",
        data: {},
        method: "POST",
        success: function (request) {
            $('#gameroomstablebody').empty();
            if(request.length > 0) {
                putGamerooms(request);
            }
            else{
                $('#gameroomstablebody').append('<tr><td>Нет элементов</td><td>Нет элементов</td><td>Нет элементов</td></tr>');
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function putGamerooms(gamerooms){
    var gameroomstablebody = "";
    var counter = 0;
    globalGameroomsList = gamerooms;
    for(i = 0; i < gamerooms.length; i++) {
        elem = gamerooms[i];
        if (!elem.isInGame) {
            master = elem.master.login;
            counter++;
            item = '<tr>' +
                ' <td>' + (i + 1) + '</td>' +
                '<td>' + master + '</td>' +
                '<td>' +
                '<button id = "room' + (i + 1) + '">Войти</button>' +
                '</td>' +
                '</tr>';
            gameroomstablebody = gameroomstablebody + item;
        }
    }
    $('#gameroomstablebody').append(gameroomstablebody);
    if(counter == 0){
        $('#gameroomstablebody').append('<tr><td>Нет элементов</td><td>Нет элементов</td><td>Нет элементов</td></tr>');
    }
    for (i = 0; i < gamerooms.length; i++) {
        if (!gamerooms[i].isInGame) {
            document.querySelector("#room" + (i + 1)).onclick = function (ev) {
                var m = globalGameroomsList[ev.toElement.id.replace("room", "") - 1].master.login;
                chooseGameRoomByMaster(m);
            }
        }
    }

}

function chooseGameRoomByMaster(master) {
    goIntoGameRoom(master);
}

function createNewGameroom(token){
    $.ajax({
        url: "/createGameRoomServlet",
        data: {token: token},
        method: "POST",
        success: function (request) {
            if(request){
                iAmMaster = true;
                hideButtonsToTeams();
                userIsMaster = true;
                document.getElementById("startGameButton").style.display = 'block';
                document.getElementById("startGameButton").onclick = function (ev) { startGame() };
                ajaxGetLoginByTokenAndGo(localStorage.getItem("token"));
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function ajaxGetLoginByTokenAndGo(userToken){
    $.ajax({
        url: "/getLoginByTokenServlet",
        data: {token: userToken},
        method: "POST",
        success: function (request) {
            goIntoGameRoom(request);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function goIntoGameRoom(masterLogin){
    clearInterval(roomListIntervalId);
    hideTable();
    setTeamsVisible();
    roomMasterLogin = masterLogin;
    drawTablesOfTeams(masterLogin);
    gameRoomIntervalId = setInterval(drawTablesOfTeams, gameRoomIntervalTime, masterLogin);
    iAmHereIntervalId = setInterval(iAmHere, iAmHereIntervalTime, masterLogin);
    if(!iAmMaster){
        waitingGameIntervalId = setInterval(waitGame, waitingGameIntervalTime);
    }
}

function hideTable(){
    document.getElementById("newgameroom").style.display = 'none';
    document.getElementById("gameroomstable").style.display = 'none';
}

function showTable(){
    document.getElementById("newgameroom").style.display = 'block';
    document.getElementById("gameroomstable").style.display = 'block';
}

function hideTeams() {
    document.getElementById("backbutton").style.display = 'none';
    document.getElementById("startGameButton").style.display = 'none';
    document.getElementById("teamstable1").style.display = 'none';
    document.getElementById("teamstable2").style.display = 'none';
}

function setTeamsVisible(){
    document.getElementById("backbutton").style.display = 'block';
    document.getElementById("teamstable1").style.display = 'block';
    document.getElementById("teamstable2").style.display = 'block';
}

function drawTablesOfTeams(masterLogin){
    $.ajax({
        url: "/getRoomServlet",
        data: {masterLogin: masterLogin},
        method: "POST",
        success: function (request) {
            if(!request){
                goBackToList();
            }
            else {
                $('#teamstable1body').empty();
                $('#teamstable2body').empty();
                drawTableOfTeam1(request);
                drawTableOfTeam2(request);
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function drawTableOfTeam1(request){
    var tablebody;
    var item;
    for(i = 0; i< request.firstTeam.length; i++){
        item = '<tr>' +
            ' <td>'+ request.firstTeam[i].login+'</td>' +
            '<td>'+
            '</td>'+
            '</tr>';
        tablebody = tablebody + item;
    }
    if(request.firstTeam.length< maxCountOfPlayersInTeam){
        for(i = request.firstTeam.length; i< maxCountOfPlayersInTeam; i++){
            item = '<tr>' +
                ' <td>'+ 'место свободно'+'</td>';
            if(i === request.firstTeam.length && userCanTakePartInTeam){
                item = item + '<td>'+'<button class = "button1">Занять место</button>'+
                    '</td>';
            }
            else{
                item = item +  '<td>'+'</td>';
            }
            item = item + '</tr>';
            tablebody = tablebody + item;
        }
    }
    $('#teamstable1body').append(tablebody);
    if(request.firstTeam.length< maxCountOfPlayersInTeam && userCanTakePartInTeam){
        document.querySelector(".button1").onclick = function (ev) {
            ev.srcElement.disabled = true;
            takePlaceInFirstTeam(request.master.login, localStorage.getItem("token"));
        }
    }
}

function drawTableOfTeam2(request) {
    var tablebody;
    var item;
    for(i = 0; i< request.secondTeam.length; i++){
        item = '<tr>' +
            ' <td>'+ request.secondTeam[i].login+'</td>' +
            '<td>'+
            '</td>'+
            '</tr>';
        tablebody = tablebody + item;
    }
    if(request.secondTeam.length< maxCountOfPlayersInTeam){
        for(i = request.secondTeam.length; i< maxCountOfPlayersInTeam; i++){
            item = '<tr>' +
                ' <td>'+ 'место свободно'+'</td>';
            if(i === request.secondTeam.length && userCanTakePartInTeam){
                item = item + '<td>'+'<button class = "button2">Занять место</button>'+
                '</td>';
            }
            else{
                item = item +  '<td>'+'</td>';
            }
            item = item + '</tr>';
            tablebody = tablebody + item;
        }
    }
    $('#teamstable2body').append(tablebody);
    if(request.secondTeam.length< maxCountOfPlayersInTeam && userCanTakePartInTeam){
        document.querySelector(".button2").onclick = function (ev) {
            ev.srcElement.disabled = true;
            takePlaceInSecondTeam(request.master.login, localStorage.getItem("token"));
        }
    }
}

function goBackToList() {
    iAmMaster = false;
    clearInterval(waitingGameIntervalId);
    clearInterval(gameRoomIntervalId);
    clearInterval(iAmHereIntervalId);
    hideTeams();
    showButtonsToTeams();
    userIsMaster = false;
    showTable();
    ajaxGetListOfGameRooms();
    roomListIntervalId = setInterval(ajaxGetListOfGameRooms, roomListIntervalTime);
}

function takePlaceInFirstTeam(masterLogin, userToken){
    $.ajax({
        url: "/takePlaceInTeam1",
        data: {masterLogin: masterLogin, userToken: userToken},
        method: "POST",
        success: function (request) {
           hideButtonsToTeams();
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function takePlaceInSecondTeam(masterLogin, userToken){
    $.ajax({
        url: "/takePlaceInTeam2",
        data: {masterLogin: masterLogin, userToken: userToken},
        method: "POST",
        success: function (request) {
            hideButtonsToTeams();
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function hideButtonsToTeams() {
    userCanTakePartInTeam = false;
}

function showButtonsToTeams() {
    userCanTakePartInTeam = true;
}

function iAmHere(masterLogin){
    $.ajax({
        url: "/IamHere",
        data: {token: localStorage.getItem("token"), masterLogin: masterLogin},
        method: "POST",
        success: function (request) {

        },
        error: function (e) {
            console.log(e);
        }
    });
}

function startGame(){
    $.ajax({
        url: "/startGame",
        data: {token: localStorage.getItem("token")},
        method: "POST",
        success: function (request) {
            if(request){
                getMyGameId();
                clearInterval(gameRoomIntervalId);
                clearInterval(iAmHereIntervalId);
                inTheBattleIntervalId = setInterval(iAmInTheBattle, inTheBattleIntervalTime);
                hideTeams();
                setGamePictureGround();
                initialiseGameObjects();
                inGame = true;
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function initialiseGameObjects(){
    $.ajax({
        url: "/getGameFrameObjects",
        data: {masterLogin: roomMasterLogin, token: localStorage.getItem("token")},
        method: "POST",
        success: function (request) {
            var img1;
            var height;
            for(i = 0; i < request.length; i++){
                for(j = 0; j < request[0].length; j++){
                    if(request[i][j] == null){
                        continue;
                    }
                    img1 = document.createElement("IMG");
                    if(request[i][j].description === "tank1"){
                        if(request[i][j].id == myGameId){
                            img1.src = "../images/gameSprites/tank0.png";
                            img1.style.zIndex = 10;
                        }
                        else{
                            img1.src = "../images/gameSprites/tank1.png";
                            img1.style.zIndex = 2;
                        }
                    }
                    else if(request[i][j].description === "tank2"){
                        if(request[i][j].id == myGameId){
                            img1.src = "../images/gameSprites/tank0.png";
                            img1.style.zIndex = 10;
                        }
                        else {
                            img1.src = "../images/gameSprites/tank2.png";
                            img1.style.zIndex = 2;
                        }
                    }
                    else if(request[i][j].description === "wall"){
                        img1.src = "../images/gameSprites/wall.bmp";
                        img1.style.zIndex = 2;
                    }
                    else if(request[i][j].description === "stone"){
                        img1.src = "../images/gameSprites/stone.bmp";
                        img1.style.zIndex = 2;
                    }
                    img1.setAttribute( "id",  request[i][j].id);
                    img1.setAttribute("class", "gameObject");
                    img1.style.position = "absolute";
                    height = 100/request[0].length;
                    img1.style.height = height + "%";
                    img1.style.top = j*height + "%";
                    img1.style.left = i*height*screen.height/screen.width + "%";
                    document.querySelector('.content').appendChild(img1);
                }
            }
            initialiseBullets();
            updateGamePictureIntervalId = setInterval(updateGamePicture, updateGamePictureIntervalTime, roomMasterLogin);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function updateGamePicture(mLogin) {
    $.ajax({
        url: "/getGameFrameObjects",
        data: {masterLogin: mLogin, token: localStorage.getItem("token")},
        method: "POST",
        success: function (request) {
            if(request == null){
                endGame();
            }
            else{
                var divsToHide = document.getElementsByClassName("gameObject");
                for (var i = 0; i < divsToHide.length; i++) {
                    divsToHide[i].style.display = "none";
                }
                var img1;
                var top;
                var left;
                var height = 100 / (request[0].length + 5); //in % (5 - reserv)
                for (i = 0; i < request.length; i++) {
                    for (j = 0; j < request[0].length; j++) {
                        if (request[i][j] == null) {
                            continue;
                        }
                        img1 = document.querySelector("#" + request[i][j].id);
                        img1.style.display = "block";
                        top = j * height;
                        left = i * height;
                        if (request[i][j].direction === 1) {
                            img1.style.transform = 'rotate(0deg)';
                        }
                        else if (request[i][j].direction === 2) {
                            img1.style.transform = 'rotate(90deg)'; // top - 1, right - 2, down - 3, left - 4 for faster loading
                        }
                        else if (request[i][j].direction === 3) {
                            img1.style.transform = 'rotate(180deg)';
                        }
                        else if (request[i][j].direction === 4) {
                            img1.style.transform = 'rotate(270deg)';
                        }
                        if (request[i][j].delta > 0) {
                            if (request[i][j].direction === 1) {
                                top = top - height * request[i][j].delta;
                            }
                            else if (request[i][j].direction === 2) {
                                left = left + height * request[i][j].delta; // top - 1, right - 2, down - 3, left - 4 for faster loading
                            }
                            else if (request[i][j].direction === 3) {
                                top = top + height * request[i][j].delta;
                            }
                            else if (request[i][j].direction === 4) {
                                left = left - height * request[i][j].delta;
                            }
                        }
                        img1.style.height = height + "%";
                        img1.style.top = top + "%";
                        img1.style.left = left * screen.height / screen.width + "%";
                    }
                }
            }
            drawBullets(height);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function setGamePictureGround(){
    $.ajax({
        url: "/GetGameFrameGround",
        data: {masterLogin: roomMasterLogin, token: localStorage.getItem("token")},
        method: "POST",
        success: function (request) {
            var img1;
            var height;
            for(i = 0; i < request.length; i++){
                for(j = 0; j < request[0].length; j++){
                    img1 = document.createElement("IMG");
                    if(request[i][j]=== "None"){
                        img1.src = "../images/gameSprites/none.bmp";
                        img1.style.zIndex = 1;
                    }
                     else if(request[i][j]=== "Grass"){
                        img1.src = "../images/gameSprites/grass.png";
                        img1.style.zIndex = 5;
                    }
                    else{
                        img1.src = "../images/gameSprites/wall.bmp";
                    }
                    img1.style.position = "absolute";
                    img1.setAttribute("class", "gameGroung");
                    height = 100/(request[0].length+5);
                    img1.style.height = height + "%";
                    img1.style.top = j*height + "%";
                    img1.style.left = i*height*screen.height/screen.width + "%";
                    document.querySelector(".content").appendChild(img1);
                }
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function setHandler(){
    addEventListener("keydown", keysListener);
}

function keysListener(ev){
    if (inGame) {
        if (ev.keyCode === 65 || ev.keyCode === 87 || ev.keyCode === 68 || ev.keyCode === 83 || ev.keyCode === 32) {
            $.ajax({
                url: "/GameCommand",
                data: {key: ev.keyCode, masterLogin: roomMasterLogin, token: localStorage.getItem("token")},
                method: "POST",
                success: function (request) {
                },
                error: function (e) {
                    console.log(e);
                }
            });
        }
    }
}

function waitGame() {
    $.ajax({
        url: "/WaitGame",
        data: {masterLogin: roomMasterLogin},
        method: "POST",
        success: function (request) {
            if(request){
                getMyGameId();
                clearInterval(gameRoomIntervalId);
                clearInterval(iAmHereIntervalId);
                clearInterval(waitingGameIntervalId);
                inTheBattleIntervalId = setInterval(iAmInTheBattle, inTheBattleIntervalTime);
                hideTeams();
                setGamePictureGround();
                initialiseGameObjects();
                inGame = true;
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function iAmInTheBattle() {
    $.ajax({
        url: "/iAmInTheBattle",
        data: {masterLogin: roomMasterLogin, token: localStorage.getItem("token")},
        method: "POST",
        success: function (request) {
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function endGame() {
    clearInterval(updateGamePictureIntervalId);
    alert("Игра окончена");
    window.location.reload();
}

function initialiseBullets() {
    $.ajax({
        url: "/getTanksId",
        data: {masterLogin: roomMasterLogin},
        method: "POST",
        success: function (request) {
            var img1;
            for (i = 0; i < request.length; i++) {
                if (request[i] == null) {
                    continue;
                }
                img1 = document.createElement("IMG");
                img1.src = "../images/gameSprites/bullet2.png";
                img1.style.zIndex = 15;
                img1.style.position = "absolute";
                img1.setAttribute("id", "bullet" + request[i]);
                img1.setAttribute("class", "bullet");
                document.querySelector('.content').appendChild(img1);
            }
        }
        ,
        error: function (e) {
            console.log(e);
        }
    });
}

function drawBullets(height) {
    $.ajax({
        url: "/getBullets",
        data: {masterLogin: roomMasterLogin},
        method: "POST",
        success: function (request) {
            var divsToHide = document.getElementsByClassName("bullet");
            for (var i = 0; i < divsToHide.length; i++) {
                divsToHide[i].style.display = "none";
            }
            if(request != null) {
                var img1;
                var top;
                var left;
                for (i = 0; i < request.length; i++) {

                    if (request[i] == null) {
                        continue;
                    }
                    img1 = document.querySelector("#bullet" + request[i].id);
                    img1.style.display = "block";
                    top = request[i].y * height;
                    left = request[i].x * height;
                    if (request[i].direction === 1) {
                        img1.style.transform = 'rotate(0deg)';
                    }
                    else if (request[i].direction === 2) {
                        img1.style.transform = 'rotate(90deg)'; // top - 1, right - 2, down - 3, left - 4 for faster loading
                    }
                    else if (request[i].direction === 3) {
                        img1.style.transform = 'rotate(180deg)';
                    }
                    else if (request[i].direction === 4) {
                        img1.style.transform = 'rotate(270deg)';
                    }
                    if (request[i].delta > 0) {
                        if (request[i].direction === 1) {
                            top = top - height * request[i].delta;
                        }
                        else if (request[i].direction === 2) {
                            left = left + height * request[i][j].delta; // top - 1, right - 2, down - 3, left - 4 for faster loading
                        }
                        else if (request[i].direction === 3) {
                            top = top + height * request[i][j].delta;
                        }
                        else if (request[i].direction === 4) {
                            left = left - height * request[i][j].delta;
                        }
                    }
                    img1.style.height = height + "%";
                    img1.style.top = top + "%";
                    img1.style.left = left * screen.height / screen.width + "%";
                }
            }
        }
        ,
        error: function (e) {
            console.log(e);
        }
    });
}
