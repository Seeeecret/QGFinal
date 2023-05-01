var ws;
$(document).ready(function () {
    var token = sessionStorage.getItem('token');
    if(token === null){
        token = localStorage.getItem('token');
        sessionStorage.setItem('token',token)
    }
    localStorage.removeItem('token');
    const urlParams = new URLSearchParams(window.location.search);
    const printerId = urlParams.get('printerId');
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/QGFinal_war/checkToken',
        headers: {
            'Authorization': token
        },
        data: {
            page: "printerDetail",
            method: "checkTokenOnly",
        },
        dataType: "json",
        success: function (data, status, jqXHR) {
            if (data.success === true) {
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
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/QGFinal_war/printer",
        data: {
            printerId: printerId,
            method: "getPrinterName"
        },
        dataType: "json",
        success: function (data, status, jqXHR) {
            if (data.success === true) {
                $("#printerName").text(data.data.printerName);
            } else {
                alert("Failed to get printers");
            }
        },
        error: function (xhr, status, error) {
            alert("System error");
        }
    });
    if (!ws || ws.readyState !== 1) {
        // 如果WebSocket不存在或连接已关闭，则创建一个新的WebSocket对象并建立连接
        ws = new WebSocket("ws:localhost:8080/QGFinal_war/websocket?printerId=" + printerId, "protocol1");
    }
// 创建一个 WebSocket 对象，指定连接的 URL 和子协议

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
        if (jsonObject.statusValue === 40) {
            $("#printerProgress").text("0%");
        } else if (jsonObject.statusValue === 41 || jsonObject.statusValue === 7) {
            $("#printerProgress").text(jsonObject.printerProgress);
        }
        if (jsonObject.statusValue === 21) {
            $("#printerTemperature").text(jsonObject.printerTemperature + "℃");
        }
        if (jQuery.isEmptyObject(jsonObject.statisticTime)) {

        } else {
            $("#onTimeToday").text(jsonObject.statisticTime.onTime);
            $("#printTimeToday").text(jsonObject.statisticTime.printTime);
            $("#idleTimeToday").text(jsonObject.statisticTime.idleTime);
            $("#exceptionTimeToday").text(jsonObject.statisticTime.exceptionTime);
        }
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

})
;