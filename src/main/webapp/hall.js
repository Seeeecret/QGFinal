// 登陆后大厅页面的js
$(document).ready(function () {
    var token = localStorage.getItem('token');
    // 使用Jquery的选择器和html方法，获取和设置元素的内容
    setInterval(function () {
        $("#currentTime").text("当前系统时间：" + new Date().toLocaleString());
    }, 1000);
    $.ajax({
        type: "POST",

        url: 'http://localhost:8080/QGFinal_war/checkToken',
        headers: {
            'Authorization': token
        },
        data: {
            page: "hall",
            method: "checkToken",
        },
        dataType: "json",
        success: function (data, status, jqXHR) {
            // 处理响应数据
            if (data.success === true) {
            } else {
                alert("Please login first");
                window.location.href = "login.html";
            }
        },
        error: function (xhr, status, error) {
            alert("System error");
            window.location.href = "login.html";
        }
    });
    $("#userCenterBtn").click(function () {
            window.location.href = "userCenter.html";
        });
    $("#logoutBtn").click(function () {
        localStorage.removeItem("username");
        localStorage.removeItem("token");
        window.location.href = "login.html";
    });

});