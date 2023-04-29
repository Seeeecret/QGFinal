$(document).ready(function () {
    var roleID;
    var token = localStorage.getItem('token');
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
            page: "userCenter",
            method: "checkToken",
        },
        dataType: "json",
        success: function (data, status, jqXHR) {
            roleID = data.data.roleId;
            // 处理响应数据
            if (data.success === true) {
                if (roleID === 3) {
                    $("#enterpriseLabel").text("您的企业")
                    $("#enterprise").hide();
                } else if (roleID === 1) {
                    $("#enterpriseLabel").hide();
                    $("#enterprise").hide();
                    $("#enterpriseCodeLabel").hide()
                    $("#enterpriseCode").hide()
                }
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
    $("#saveBtn").click(function () {
            let email = $("#email").val();
            let phone = $("#phone").val();
            let sex = $("#sex").val();
            let enterprise = $("#enterprise").val();
            let enterpriseCode = $("#enterpriseCode").val();
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: 'http://localhost:8080/QGFinal_war/user',
                headers: {
                    'Authorization': token
                },
                data: {
                    email: email,
                    phone: phone,
                    sex: sex,
                    enterprise: enterprise,
                    enterpriseCode: enterpriseCode,
                    page: "userCenter",
                    method: "updateUserInfo",
                },
                dataType: "json",
                success: function (data, status, jqXHR) {
                    // 处理响应数据
                    if (data.success === true) {
                        alert("修改成功");
                    } else {
                        alert("修改失败")
                    }
                },
                error: function (xhr, status, error) {
                    alert("System error");
                    window.location.href = "login.html";
                }
            });
        });
    $("#logoutBtn").click(function () {
        localStorage.removeItem("username");
        localStorage.removeItem("token");
        window.location.href = "login.html";
    });
    $("#hallBtn").click(function () {
        window.location.href = "hall.html";
    });
});