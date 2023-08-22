package com.dayu.websocket.utils;

import com.dayu.websocket.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageUtils {
    public static String getMessage(Boolean isSystem,String fromName,Object message){
        try {
            com.dayu.websocket.Message result=new Message();
            result.setIsSystem(isSystem);
            if (fromName!=null){
                result.setFromName(fromName);
            }
            result.setMessage(message);
            //转json字符串
            ObjectMapper mapper=new ObjectMapper();

            return mapper.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
