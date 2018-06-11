$(document).bind('afterready', function() {
    ajaxGetAchievementsList();
});

function ajaxGetAchievementsList(){
    var i;
    var elem;
    var achlist;
    var temp = "";
    $.ajax({
        url: "/achievementsListServlet",
        method: "POST",
        data: {token: localStorage.getItem("token")},
        success: function (request) {
            achlist = request;
            for(i = 0; i < achlist.length; i++){
                elem = achlist[i];
                temp = temp + "<tr>" +
                    "<td>" + elem.name +"</td>\n" +
                    "<td>"+ elem.description+ "</td>\n" +
                    "<td>"+ getComplitedFlag(elem)+ "</td>" +
                    "</tr>";
            }
            document.querySelector('#achievementList').innerHTML = temp;
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function getComplitedFlag(elem) {
    if(elem.complited){
        return "+";
    }
    else{
        return "-"
    }
}