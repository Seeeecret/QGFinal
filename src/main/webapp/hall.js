// 登陆后大厅页面的js
$(document).ready(function () {
    var roleId;
    var username;
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
            roleId = data.data.roleId;
            username = data.data.username;
            // 处理响应数据
            if (data.success === true) {
                $("#title").text("欢迎用户 "+username+" 进入大厅");
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
    $("#showPrintersBtn").click(function () {
        // 获取用户的roleId和username
        var username = localStorage.getItem("username");

        // 发送请求
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/QGFinal_war/printer",
            data: {
                roleId: roleId,
                username: username ,
                method: "getPrinter"
            },
            dataType: "json",
            success: function (data, status, jqXHR) {
                // 处理响应数据
                if (data.success === true) {
                    // 显示打印机链接
                    var printers = data.data.printerList;
                    for (var i = 0; i < printers.length; i++) {
                        var printerLink = "<a href='printerDetail.html?printerId=" + printers[i].printerId + "'>" + printers[i].name + "</a>";
                        $("#printerLinks").append(printerLink);
                    }
                } else {
                    alert("Failed to get printers");
                }
            },
            error: function (xhr, status, error) {
                alert("System error");
            }
        });
    });

});