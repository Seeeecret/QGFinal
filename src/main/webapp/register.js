// import * as $ from "https://lf3-cdn-tos.bytecdntp.com/cdn/expire-1-M/jquery/3.6.0/jquery.min.js";
// function addScript(url){
//     document.write("<script language=javascript src="+url+"></script>");
// }
// addScript("https://lf3-cdn-tos.bytecdntp.com/cdn/expire-1-M/jquery/3.6.0/jquery.min.js");
$(document).ready(function () {
    const $usernameInput = $('#username');
    const $passwordInput = $('#password');
    const $usernameError = $('#username-error');
    const $passwordError = $('#password-error');
    const $submitBtn = $('#submit-btn');

// 绑定事件处理函数
    $usernameInput.on('input', validateUsername);
    $passwordInput.on('input', validatePassword);

    function validateUsername() {
        // 获取输入框中的值，并去除两端的空白字符
        const username = $usernameInput.val().trim();

        // 正则表达式验证
        const reg = /^[a-zA-Z0-9]{1,16}$/;
        if (reg.test(username)) {
            // $usernameInput.removeClass('error');
            $usernameError.text('');
        } else {
            // $usernameInput.addClass('error');
            $usernameError.text('用户名必须由非中文字符组成，长度在1-16之间');
        }

        // 判断是否所有输入框都验证通过，决定提交按钮是否可用
        if (username && $passwordInput.val()) {
            $submitBtn.prop('disabled', false);
        } else {
            $submitBtn.prop('disabled', true);
        }
    }

    function validatePassword() {
        // 获取输入框中的值，并去除两端的空白字符
        const password = $passwordInput.val().trim();

        // 正则表达式验证
        const reg = /^[a-zA-Z0-9]{1,16}$/;
        if (reg.test(password)) {
            // $passwordInput.removeClass('error');
            $passwordError.text('');
        } else {
            // $passwordInput.addClass('error');
            $passwordError.text('密码必须由非中文字符组成，长度在6-16之间');
        }

        // 判断是否所有输入框都验证通过，决定提交按钮是否可用
        if ($usernameInput.val() && password) {
            $submitBtn.prop('disabled', false);
        } else {
            $submitBtn.prop('disabled', true);
        }
    }
});