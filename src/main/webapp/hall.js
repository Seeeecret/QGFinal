$(document).ready(function() {
    var token = localStorage.getItem('token');
    // 使用Jquery的选择器和html方法，获取和设置元素的内容
    setInterval(function() {
        $("#currentTime").text("当前系统时间："+new Date().toLocaleString());
    }, 1000);
// 将token添加到请求头中
    $.ajax({
        type: "POST",

        url: '/http://localhost:8080/QGFinal_war/checklogin',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        data: {
            page:"hall",
            method:"checkLogin",
        },
        dataType: "json",
        success: function(response) {
            // 处理响应数据
        },
        error: function(xhr, status, error) {
            alert("Not logged in");
        }
    });
    $("#logoutBtn").click(function () {
        localStorage.removeItem("username");
        localStorage.removeItem("token");
        window.location.href = "login.html";
    });

});