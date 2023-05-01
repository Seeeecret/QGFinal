package controller;

import utils.MyIOUtil;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

// 使用 @ServerEndpoint 注解来标注这个类是一个 WebSocket 服务端，并指定一个访问路径
@ServerEndpoint(value = "/websocket", configurator = MyEndpointConfigurator.class)
public class WebSocketServer {

    // 定义一个静态变量来存储所有已连接的客户端会话对象
    private static HashMap<Integer, CopyOnWriteArrayList<Session>> sessionsMapList = new HashMap<>();
    private static CopyOnWriteArrayList<Session> sessionsList = new CopyOnWriteArrayList<>();

    // 使用 @OnOpen 注解来标注这个方法会在客户端连接成功时调用，并将客户端会话对象添加到静态变量中
    @OnOpen
    public void onOpen(Session session) {
        int printerId = Integer.parseInt((String) session.getUserProperties().get("printerId"));
        if (sessionsMapList.containsKey(printerId)) {
            sessionsMapList.get(printerId).add(session);
        } else {
            CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();
            sessions.add(session);
            sessionsMapList.put(printerId, sessions);
        }
        System.out.println("有新的客户端连接，当前在线人数为："+ getHashMapSize(sessionsMapList)+ ",printerId= "+printerId);
    }


    // 使用 @OnClose 注解来标注这个方法会在客户端断开连接时调用，并将客户端会话对象从静态变量中移除
    @OnClose
    public void onClose(Session session) {
        int printerId = Integer.parseInt((String) session.getUserProperties().get("printerId"));
        if (sessionsMapList.containsKey(printerId)) {
            sessionsMapList.get(printerId).remove(session);
        }else{
            System.out.println("发生错误,sessionsMapList中没有printerId= "+printerId);
            throw new RuntimeException("发生错误,sessionsMapList中没有printerId= "+printerId);
        }
        System.out.println("有客户端断开连接，当前在线人数为：" + sessionsList.size());
    }

    // 使用 @OnMessage 注解来标注这个方法会在接收到客户端发送的消息时调用，并根据消息内容查询数据库中的相应字段信息
    @OnMessage
    public void onMessage(String message, Session session) {
        String jsonMessage = MyIOUtil.URLtoJson(message);
//        JSONObject jsonObject = JSON.parseObject(jsonMessage);
//        Integer printerId = jsonObject.getInteger("printerId");
//        for (Session tempSession : sessionsList) {
//            if (sessionsMapList.containsKey(printerId)) {
//                sessionsMapList.get(printerId).add(tempSession);
//            } else {
//                CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();
//                sessions.add(tempSession);
//                sessionsMapList.put(printerId, sessions);
//            }
//        }
        System.out.println("收到客户端的消息：" + message);
    }


    // 使用 @OnError 注解来标注这个方法会在发生错误时调用，并打印错误信息
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    // 定义一个静态方法，用于向所有已连接的客户端会话对象发送消息
    public static void broadcast(String message, int printerId) throws IOException {
        for (Map.Entry<Integer, CopyOnWriteArrayList<Session>> entry : sessionsMapList.entrySet()) {
            if (entry.getKey().equals(printerId)) {
                for (Session session : entry.getValue()) {
                    session.getBasicRemote().sendText(message);
                }
                break;
            }
        }
    }

    public int getOnlineCount() {
        int count = 0;
        for (Map.Entry<Integer, CopyOnWriteArrayList<Session>> entry : sessionsMapList.entrySet()) {
            for (Session session : entry.getValue()) {
                count++;
            }
        }
        return count;
    }
    public static int getHashMapSize(HashMap<Integer, CopyOnWriteArrayList<Session>> sessionsMap) {
        int count = 0;
        for (Map.Entry<Integer, CopyOnWriteArrayList<Session>> entry : sessionsMap.entrySet()) {
            for (Session session : entry.getValue()) {
                count++;
            }
        }
        return count;
    }
}

