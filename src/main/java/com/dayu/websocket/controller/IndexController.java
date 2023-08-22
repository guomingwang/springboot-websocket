package com.dayu.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/getLogin")
    public String getLogin(){
        return "login";
    }

    @RequestMapping("/getMain")
    public String getMain(){
        return "main";
    }
}
