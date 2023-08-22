package com.dayu.websocket;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public static Result success(String msg,Object data){
        Result result=new Result();
        result.code=200;
        result.msg=msg;
        result.data=data;
        return result;
    }

    public static Result success(String msg){
        Result result=new Result();
        result.code=200;
        result.msg=msg;
        return result;
    }

    public static Result error(String msg){
        Result result=new Result();
        result.code=500;
        result.msg=msg;
        return result;
    }
}
