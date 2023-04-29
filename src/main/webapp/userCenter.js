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
                if (roleID === 2) {
                    $("#enterpriseLabel").text("您的企业")
                    $("#enterprise").hide();
                } else if (roleID === 1) {
                    $("#enterpriseLabel").hide();
                    $("#enterprise").hide();
                    $("#enterpriseCodeLabel").hide()
                    $("#enterpriseCode").hide()
                } else {
                    $("#enterpriseLabel").text("所绑定企业名称")
                    $("#enterpriseLabel").show();
                    $("#enterprise").show();
                    $("#enterpriseCodeLabel").show()
                    $("#enterpriseCode").show()
                }
                queryUserInfo();
            } else {
                alert("Please login first");
                window.location.href = "login.html";
            }
        },
        error: function (xhr, status, error) {
            alert("System error");
            // window.location.href = "login.html";
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
                if (data.code === 206) {
                    queryUserInfo();
                    alert("个人信息修改成功,绑定企业操作失败");
                } else if (data.code === 200) {
                    alert("绑定企业操作失败和个人信息修改均已成功");
                } else
                    alert("修改失败")
            },
            error: function (xhr, status, error) {
                alert("System error");
                // window.location.href = "login.html";
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

function queryUserInfo() {
    // 检查token是否存在
    let token = localStorage.getItem("token");
    if (token != null) {
        // 发送请求
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: 'http://localhost:8080/QGFinal_war/user',
            headers: {
                'Authorization': token
            },
            data: {
                page: "userCenter",
                method: "queryUserInfo",
            },
            dataType: "json",
            success: function (data, status, jqXHR) {
                let Info = JSON.parse(data.data.userInfo);
                $("#username").val(localStorage.getItem("username"));
                $("#sex").val(Info.sex);
                $("#email").val(Info.email);
                $("#phone").val(Info.phone);
                $("#enterprise").val(Info.enterprise);
                $("#enterpriseCode").val(Info.enterpriseCode);
            },
            error: function (xhr, status, error) {
                alert("System error occur when query user info");
                window.location.href = "hall.html";
            }
        });
    }
}
