package com.movie.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{userId}")
@Component
@Slf4j
public class WebSocketServer {
    // 静态变量，用来记录当前在线连接数
    private static int onlineCount = 0;

    // ConcurrentHashMap: 线程安全，用来存放每个客户端对应的 WebSocketServer 对象
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 当前连接的用户ID
    private String userId;

    //连接建立成功调用的方法
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;

        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else {
            webSocketMap.put(userId, this);
            addOnlineCount();
        }

        log.info("用户连接: " + userId + ", 当前在线人数: " + getOnlineCount());
    }

    //连接关闭调用的方法
    @OnClose
    public void onClose(){
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineCount();
        }
        log.info("用户退出: " + userId + ", 当前在线人数: " + getOnlineCount());
    }

    //收到客户端消息后调用的方法
    @OnMessage
    public void onMessage(String message, Session session){
        log.info("收到用户消息: " + userId + ", 报文: " + message);
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable error){
        log.error("用户错误: " + this.userId + ", 错误信息: " + error.getMessage());
        error.printStackTrace();
    }

    //实现服务器主动推送 (点对点)
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //发送自定义消息给指定用户
    public static void sendInfo(String message, String userId){
        if (webSocketMap.containsKey(userId)) {
            try {
                webSocketMap.get(userId).sendMessage(message);
            } catch (IOException e) {
                log.error("发送消息失败: " + userId);
            }
        } else {
            log.info("用户 " + userId + " 不在线");
        }
    }

    //群发消息给所有人
    public static void sendToAll(String message) {
        for (String key : webSocketMap.keySet()) {
            try {
                webSocketMap.get(key).sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
