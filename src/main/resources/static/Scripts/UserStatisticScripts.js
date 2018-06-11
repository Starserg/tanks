$(document).bind('afterready', function() {
    var token = localStorage.getItem("token");
    document.querySelector("#buttonexit").onclick = function (ev) { 
        exit(token);
    };
    ajaxGetUserStatistic(token);
    ajaxgetEmail(token)
});


function ajaxGetUserStatistic(token){
    $.ajax({
        url: "/userStatisticServlet",
        data: {token: token},
        method: "POST",
        success: function (request) {
            document.querySelector('#userLogin').innerHTML = document.querySelector('#userLogin').innerHTML + "   " + request.userLogin;
            document.querySelector('#percentOfWins').innerHTML = document.querySelector('#percentOfWins').innerHTML + "   " + parseInt(request.winsStatistic*100) + "%";
            document.querySelector('#battles').innerHTML = document.querySelector('#battles').innerHTML + "   " + request.battles;
            document.querySelector('#kills').innerHTML = document.querySelector('#kills').innerHTML + "   " + request.kills;
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function exit(token) {
    $.ajax({
        url: "/exit",
        data: {token: token},
        method: "POST",
        success: function (request) {
            if(request){
                location.replace("/authorization");
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function ajaxgetEmail(token) {
    $.ajax({
        url: "/getEmail",
        data: {token: token},
        method: "POST",
        success: function (request) {
            document.querySelector('#userEmail').innerHTML = document.querySelector('#userEmail').innerHTML + "   " + request;
        },
        error: function (e) {
            console.log(e);
        }
    });
}