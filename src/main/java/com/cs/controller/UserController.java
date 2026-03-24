package com.cs.controller;

import com.cs.dto.LoginForm;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    //  登录
    @RequestMapping("/login")

    public String login(@RequestBody LoginForm form, HttpSession session){

    }
}


//    Spring MVC 在调用这个方法前，会从当前的 HttpServletRequest 中获取 HttpSession 对象。
//  获取逻辑等价于：request.getSession()（如果会话不存在，则自动创建）。
//  然后将这个对象作为参数传入方法，供你操作会话属性。
//  同一个用户发起的多个请求，即使由不同的线程处理，这些线程都能访问到同一个 Session 对象，因此调用 setAttribute 后，该用户后续请求都能读取到新设置的值。