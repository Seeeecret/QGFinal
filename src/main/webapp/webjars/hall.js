// 登陆后大厅页面的js
// var links= [];
$(document).ready(function () {
    var roleId;
    var username;
    var token = sessionStorage.getItem('token');
    if(token === null){
        alert("Please login first");
        window.location.href = "login.html";
    }
    // 使用Jquery的选择器和html方法，获取和设置元素的内容
    setInterval(function () {
        $("#currentTime").text("当前系统时间：" + new Date().toLocaleString());
    }, 1000);
    $.ajax({
        type: "POST",
        url: 'http://192.168.88.130:8080/QGFinal/checkToken',
        headers: {
            'Authorization': token
        },
        data: {
            page: "hall",
            method: "checkTokenRole",
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
        sessionStorage.removeItem("username");
        sessionStorage.removeItem("token");
        window.location.href = "login.html";
    });
    $("#showPrintersBtn").click(function () {
        // 获取用户的roleId和username
        var username = sessionStorage.getItem("username");

        // 发送请求
        $.ajax({
            type: "POST",
            url: "http://192.168.88.130:8080/QGFinal/printer",
            data: {
                roleId: roleId,
                username: username ,
                method: "getPrinter"
            },
            dataType: "json",
            success: function (data, status, jqXHR) {
                $("#showPrintersBtn").attr("disabled", true);
                // 处理响应数据
                if (data.success === true) {
                    // 显示打印机链接
                    var printers = data.data.printerList;
                    for (var i = 0; i < printers.length; i++) {
                        var printerLink = "<a target='_blank' class='jumpToPrinterDetail' href='printerDetail.html?printerId=" + printers[i].printerId + "'> " + printers[i].name + " </a>";                        $("#printerLinks").append(printerLink);
                    }
                    links=$(".jumpToPrinterDetail");
                } else {
                    alert("Failed to get printers");
                }
            },
            error: function (xhr, status, error) {
                alert("System error");
            }
        });
    });
    $("#printerLinks").click(function () {
        localStorage.setItem("token", token);
    });

});