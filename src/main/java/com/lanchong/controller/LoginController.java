package com.lanchong.controller;

import com.lanchong.pojo.LoginParam;
import com.lanchong.result.Result;
import com.lanchong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @program: SeckillProject
 * @description: 登录表现层
 **/
@Controller
@RequestMapping("/user")
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public  Result<String> doLogin(@Valid LoginParam loginParam,HttpServletResponse response) {
        System.out.println(loginParam);
        //登录
        String token = userService.login(response,loginParam);
        return Result.success(token);
    }
}
