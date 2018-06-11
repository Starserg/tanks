window.onload = function(){

    document.querySelector('.buttonlogin').onclick = function () {
        var login = document.querySelector('#login').value;
        var password = document.querySelector('#password').value;
        ajaxCheckUserData(login, password);
    }
}

function ajaxCheckUserData(login, password){
    $.ajax({
        url: "/authorizationCheckServlet",
        method: "POST",
        data: {login: login, password: password},
        success: function (request) {
            if(request){
                authorize(login, password);
            }
            else{
                alert("некорректно заполнены поля");
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function authorize(login, password){
    $.ajax({
        url: '/getToken',
        method: "POST",
        data: {login: login, password: password},
        success: function (answer) {
            localStorage.setItem("token", answer);
            location.replace("/index");
        },
        error: function (e) {
            console.log("ERROR");
            console.log(e);
        }
    });
}



