package com.dayu.websocket.controller;

import com.dayu.websocket.Result;
import com.dayu.websocket.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class UserController {
    @RequestMapping("/login")
    public Result login(User user, HttpSession session) {
        if (user.getPasswd() != null && user.getPasswd().equals("123")) {
            session.setAttribute("user", user.getUserName());
        } else {
            return Result.error("密码错误,请重试");
        }
        return Result.success("登录成功");
    }

    @RequestMapping("/getUsername")
    public Result getUsername(HttpSession session) {
        return Result.success((String) session.getAttribute("user"));
    }

    @RequestMapping("/")
    public void redirect(HttpSession session, HttpServletResponse response) throws IOException {
        Object user = session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("http://localhost/login.html");
        } else {
            response.sendRedirect("http://localhost/main.html");
        }
    }
}
