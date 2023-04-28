// <script src="webjars/jquery/3.6.4/jquery.js"></script>

$(document).ready(function() {
    $('form').submit(function(event) {
        event.preventDefault();
        var username = $('#username').val();
        var password = $('#password').val();
        if (username === '' || password === '') {
            $('.error-message').text('Please enter both username and password');
        } else {
            // 这里可以使用Ajax发送POST请求到后端进行验证
            // 如果验证成功则跳转到主页，否则显示错误信息
            // 以下是伪代码
            // $.post('/login', {username: username, password: password}, function(response) {
            //   if (response.success) {
            //     window.location.href = '/home';
            //   } else {
            //     $('.error-message').text(response.message);
            //   }
            // });
            $('.error-message').text('Invalid username or password');
        }
    });
    $("#register-link").click(function() {
        window.location.href = "register.html";
    });
});
// var registerLink = document.getElementById("register-link");
// registerLink.addEventListener("click", function() {
//     window.location.href = "register.html"; // 跳转到注册页面的 URL
// });
