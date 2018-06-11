$(document).ready(function () {
    $.ajax({
        url: "/checkToken",
        data: {token: localStorage.getItem("token")},
        method: "POST",
        success: function (request) {
            if (!request) {
                alert("Чтобы попасть на эту страницу, сначала пройдите авторизацию или зарегистрируйтесь");
                location.replace("/authorization");
            }
            else {
                document.querySelector("#buttonExitInMenu").style.display = "block";
                document.querySelector("#buttonExitInMenu").onclick = function (ev) {
                    exit(localStorage.getItem("token"));
                };
                var pageName = document.querySelector("#pageName").innerHTML;
                $.ajax({
                    url: "/getContent",
                    data: {token: localStorage.getItem("token"), pageName: pageName},
                    method: "POST",
                    success: function (request) {
                        var content = request;
                        document.querySelector(".content").innerHTML = content;
                        $(document).trigger('afterready');
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
});



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



