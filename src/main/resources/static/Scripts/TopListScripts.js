$(document).bind('afterready', function() {
    initialise();
});


function ajaxGetTopList(){
    var i;
    var elem;
    $.ajax({
        url: "/topListServlet",
        method: "POST",
        data: {},
        success: function (request) {
            toplist = request;
            for(i = 0; i < toplist.length; i++){
                elem = toplist[i];
                document.querySelector('#login' + (i+1)).innerHTML = elem.userLogin;
                document.querySelector('#stat' + (i+1)).innerHTML = parseInt(elem.winsStatistic*100) + "%";
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function initialise() {
    ajaxGetTopList();
    var button = document.getElementById('buttonTransform');
    button.onclick = function () {
        var contentfieldtop = document.getElementById('contentfieldtop');
        var contentfield = document.getElementById('contentfield');
        showOrHide(contentfieldtop);
        showOrHide(contentfield);
        setCorrectButtonValue(document.getElementById('buttonTransform'));
    };
    document.querySelector("#buttonSearch").onclick = function (ev) {
        ajaxGetUserStatistic();
    }
}

function showOrHide(element) {
    if (element.style.display == "none"){
        element.style.display = "block";
    }
    else{
        element.style.display = "none";
    }
}

function setCorrectButtonValue(button) {
    if(button.value === "найти статистику игрока"){
        button.value = "показать топ 10";
    }
    else{
        button.value = "найти статистику игрока";
    }
}


function ajaxGetUserStatistic() {
    var login = document.querySelector("#search").value;
    $.ajax({
        url: "/searchUserStatisticServlet",
        method: "POST",
        data: {login: login},
        success: function (request) {
            if(request == null){
                document.querySelector("#loginField").innerHTML = "Ник: не найден";
                document.querySelector("#battlesField").innerHTML = "Количество боев: ";
                document.querySelector("#killsField").innerHTML = "Уничтожено танков: ";
                document.querySelector("#winsField").innerHTML = "Процент побед: ";
            }
            else{
                document.querySelector("#loginField").innerHTML = "Ник: " + request.userLogin;
                document.querySelector("#battlesField").innerHTML = "Количество боев: " + request.battles;
                document.querySelector("#killsField").innerHTML = "Уничтожено танков: " + request.kills;
                document.querySelector("#winsField").innerHTML = "Процент побед: " + parseInt(request.winsStatistic*100)+"%";
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}