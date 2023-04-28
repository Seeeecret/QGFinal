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
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/QGFinal_war/user',
            data: {
                username: username,
                password: password,
                method: "login",
                remember: remember
            },
            dataType: "json",
            success: function (response, xhr) {
                let jsonData = JSON.parse(response.data);
                var token = xhr.getResponseHeader("Authorization")
                if (jsonData.success === true) {
                    localStorage.setItem("username", username);
                    localStorage.setItem("token", token);
                    alert("Successfully login");
                    window.location.href = "http://localhost:8080/QGFinal_war/meritMenu.html?username=" + localStorage.getItem("username");

                } else {

                    alert("Username or password is incorrect");

                }
            },
            error: function () {
                alert("System error");
            }
        });
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
