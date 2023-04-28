$(document).ready(function () {
    var token = localStorage.getItem('token');
    setInterval(function() {
        $("#currentTime").text("当前系统时间："+new Date().toLocaleString());
    }, 1000);

    $("#logoutBtn").click(function () {
        localStorage.removeItem("username");
        localStorage.removeItem("token");
        window.location.href = "login.html";
    });
    $("#hallBtn").click(function () {

        window.location.href = "hall.html";
    });
});