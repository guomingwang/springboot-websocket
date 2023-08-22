package com.dayu.websocket.ws;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfiguration extends ServerEndpointConfig.Configurator {

    //将request里的httpsession映射到websocket里的httpsession
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession=(HttpSession)request.getHttpSession();
        //将httpSession对象存储到配置对象中
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
