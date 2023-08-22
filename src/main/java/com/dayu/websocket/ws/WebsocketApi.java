package com.dayu.websocket.ws;

import com.dayu.websocket.Message;
import com.dayu.websocket.utils.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint(value = "/chat/{userName}",configurator = GetHttpSessionConfiguration.class)
@Component
public class WebsocketApi {
    //用来存放每个客户端对应的WebsocketApi对象，适用于同时与多个客户端通信
//    public static CopyOnWriteArraySet<WebsocketApi> webSocketSet = new CopyOnWriteArraySet<WebsocketApi>();
    //若要实现服务端与指定客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    // 为每个远程用户保存一个ServerEndpoint
    public static ConcurrentHashMap<String,WebsocketApi> webSocketMap = new ConcurrentHashMap();
    //与某个客户端的连接会话，通过它实现定向推送(只推送给某个用户)
    // 存放远程连接用户
    private Session session;
    // 存放当前登录用户
    private HttpSession httpSession;


    //建立连接时发送系统广播
    @OnOpen
    public void onOpen(Session session,EndpointConfig config,@PathParam("userName") String userName){
        System.out.println("进入连接");
        // 存放远程连接用户
        this.session = session;
        // 存放当前登录用户
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession=httpSession;
        // 为每个远程用户保存一个ServerEndpoint
        System.out.println("user:" + httpSession.getAttribute("user") + " -> ServerEndpoint:" + this.hashCode() );
        webSocketMap.put((String) httpSession.getAttribute("user"),this); //加入map中
        //将当前在线用户用户名推送给客户端
        String msg=MessageUtils.getMessage(true,null,getNames());
        //调用方法进行推送
        broadcastAllUsers(msg);
        log.info("websocket消息: 有新的连接，总数为:" + webSocketMap.size());
    }

    private void broadcastAllUsers(String message){
        //要将该消息推送给所有的客户端
        Set<String> names = webSocketMap.keySet();
        for (String name:names) {
            WebsocketApi websocketApi = webSocketMap.get(name);
            try {
                websocketApi.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Set<String> getNames(){
        return webSocketMap.keySet();
    }

    //接收到客户端消息时，用户之间的信息发送
    @OnMessage
    public void onMessage(String message,@PathParam("userName") String userName) {
        System.out.println("接收消息并发送");
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            //接收到的消息
            Message msg=objectMapper.readValue(message,Message.class);
            //将数据发送给数据接收方
            String toName=msg.getToName();
            //获取消息数据
            Object data = msg.getMessage();
            //获取当前登陆用户
            String name = (String) httpSession.getAttribute("user");
            //获取推送给指定用户消息格式的数据
            String resultMsg = MessageUtils.getMessage(false, name, data);
            //发送数据
            WebsocketApi websocketApi = webSocketMap.get(toName);
            websocketApi.session.getBasicRemote().sendText(resultMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Session session = webSocketMap.get((String) httpSession.getAttribute("user")).session;
        if (session != null && session.isOpen()) {
            try {
                log.info("websocket消: 单点消息:" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //用户断开连接的断后操作
    @OnClose
    public void onClose(Session session){
        System.out.println("断开连接");
        webSocketMap.remove(httpSession.getAttribute("user")); //从map中删除
        //将当前在线用户用户名推送给客户端
        String msg=MessageUtils.getMessage(true,null,getNames());
        //调用方法进行推送
        broadcastAllUsers(msg);
    }

    @OnError
    public void onError(Session session, Throwable error) {

        System.out.println("发生错误");

        error.printStackTrace();

    }
}
