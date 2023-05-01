$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const printerId = urlParams.get('printerId');

    // 创建一个 WebSocket 对象，指定连接的 URL 和子协议
    var ws = new WebSocket("ws:localhost:8080/QGFinal_war/websocket", "protocol1");

    // 定义 onopen 事件处理函数，当连接建立时触发
    ws.onopen = function () {
        console.log("连接已建立");
        // 使用 send 方法向服务器发送消息
        ws.send("printerId=" + printerId);
    };

    // 定义 onmessage 事件处理函数，当接收到服务器数据时触发
    ws.onmessage = function (event) {
        let jsonObject = JSON.parse(event.data);
        $("#printerStatus").text(jsonObject.statusValue);
        $("#printerReason").text(jsonObject.shortDescription + " " + jsonObject.paramDescription);
        $("#printerTemperature").text(jsonObject.temperature + "℃");
        console.log("收到服务器的消息：" + event.data);
        // alert("收到服务器的消息：" + event.data);
        // 将服务器发送的数据显示在页面上
        // 使用 jQuery 的 append 方法向 body 元素添加一个 p 元素，并设置其文本内容为 event.data
        // $("body").append($("<p></p>").text(event.data));
    };

    // 定义 onerror 事件处理函数，当发生错误时触发
    ws.onerror = function (error) {
        console.log("发生错误");
        console.log(error);
    };

    // 定义 onclose 事件处理函数，当连接关闭时触发
    ws.onclose = function (event) {
        console.log("连接已关闭");
        console.log(event);
    };
    $("#backBtn").click(function () {
        window.location.href = "hall.html";
    });
    $("#startBtn").click(function () {
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/QGFinal_war/txtData",
            data: {
                printerId: printerId,
                method: "txtDataThread"
            },
            dataType: "json",
            success: function (data, status, jqXHR) {
                alert("开始监听");
            },
            error: function (xhr, status, error) {
                alert("系统错误");
            }
        })
    });
});