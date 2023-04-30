// 用于登录页面的JavaScript代码
$(document).ready(function () {
    // let usernameCookie = $.cookie("username");
    // let passwordCookie = $.cookie("password");
    let usernameCookie = getCookie("username");
    let passwordCookie = getCookie("password");
    $("#username").attr("value", usernameCookie);
    $("#password").attr("value", passwordCookie);
    $("#loginBtn").click(function () {
        let username = $("#username").val();
        let password = $("#password").val();
        let remember = $("#rememberBtn").prop("checked");
        if (username && password) {
            $("#usernameOrPassword-error").text("");
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: 'http://localhost:8080/QGFinal_war/user',
                data: {
                    username: username,
                    password: password,
                    method: "login",
                    remember: remember
                },
                dataType: "json",
                success: function (data, status, jqXHR) {
                    let jsonData = data;
                    if (jsonData.success === true) {
                        var token = jqXHR.getResponseHeader("Authorization")
                        localStorage.setItem("username", username);
                        localStorage.setItem("token", token);
                        alert("Successfully login");
                        window.location.href = "http://localhost:8080/QGFinal_war/hall.html?username=" + localStorage.getItem("username");

                    } else {

                        alert("Username or password is incorrect");

                    }
                },
                error: function (data, status, jqXHR) {
                    if (data.responseJSON.code === 400 && data.responseJSON.success === false) {
                        alert("Username or password is incorrect");
                    } else {
                        alert("System error");
                    }
                }
            });
        } else {
            $("#usernameOrPassword-error").text("用户名或密码不能为空");
            // alert("Username or password can not be empty");
        }
    });
    $("#register-link").click(function () {
        window.location.href = "register.html";
    });
});

function getCookie(name) {
    var cookieString = document.cookie;
    var cookies = cookieString.split('; ');
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var separatorIndex = cookie.indexOf('=');
        var cookieName = cookie.substring(0, separatorIndex);
        var cookieValue = cookie.substring(separatorIndex + 1);
        if (cookieName === name) {
            return cookieValue;
        }
    }
    return null;
}
