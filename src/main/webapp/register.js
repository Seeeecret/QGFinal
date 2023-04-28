// 用于注册页面的表单验证
$(document).ready(function () {
    const usernameInput = $('#username');
    const passwordInput = $('#password');
    const usernameError = $('#username-error');
    const passwordError = $('#password-error');
    const registerBtn = $('#registerBtn');
    let usernameCorrect = false;
    let passwordCorrect = false;

// 绑定事件处理函数
    usernameInput.on('input', validateUsername);
    passwordInput.on('input', validatePassword);

    function validateUsername() {
        // 获取输入框中的值，并去除两端的空白字符
        const username = usernameInput.val().trim();

        // 正则表达式验证
        const reg = /^[a-zA-Z0-9]{1,16}$/;
        if (reg.test(username)) {
            usernameInput.removeClass('error');
            usernameError.text('');
            usernameCorrect = true;
        } else {
            usernameInput.addClass('error');
            usernameError.text('用户名格式错误');
            usernameCorrect = false;
        }

        // 判断是否所有输入框都验证通过，决定提交按钮是否可用
        if (username && passwordInput.val() && passwordCorrect && usernameCorrect) {
            registerBtn.prop('disabled', false);
        } else {
            registerBtn.prop('disabled', true);
        }
    }

    function validatePassword() {
        // 获取输入框中的值，并去除两端的空白字符
        const password = passwordInput.val().trim();

        // 正则表达式验证
        const reg = /^[a-zA-Z0-9]{8,20}$/;
        if (reg.test(password)) {
            passwordInput.removeClass('error');
            passwordError.text('');
            passwordCorrect = true;
        } else {
            passwordInput.addClass('error');
            passwordError.text('密码格式错误');
            passwordCorrect = false;
        }

        // 判断是否所有输入框都验证通过，决定提交按钮是否可用
        if (usernameInput.val() && password && passwordCorrect && usernameCorrect) {
            registerBtn.prop('disabled', false);
        } else {
            registerBtn.prop('disabled', true);
        }
    }

    $("#registerBtn").click(function () {
        let username = $("#username").val();
        let password = $("#password").val();
        let roleId = $("#role").val();
        $.ajax({
            type: "GET",
            url: 'http://localhost:8080/QGFinal_war/user',
            data: {
                username: username,
                password: password,
                roleId: roleId,
                method: "register"
            },
            success: function (response) {
                if (response.code === 200) {
                    alert("Successfully register");

                } else {
                    alert("Username is already exist");
                }
            },
            error: function () {
                alert("System error");
            }
        });
    });
});