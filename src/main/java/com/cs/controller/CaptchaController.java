package com.cs.controller;

import com.cs.tool.CaptchaUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class CaptchaController {

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成验证码
        CaptchaUtil.CaptchaInfo captcha = CaptchaUtil.generateCaptcha();

        // 将验证码文本存入 Session（设置超时时间可选）
        HttpSession session = request.getSession();
        session.setAttribute("captcha", captcha.getText());
        // 记录生成时间（用于超时控制）
        session.setAttribute("captchaTime", System.currentTimeMillis());

        // 禁止浏览器缓存
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        response.getOutputStream().write(captcha.getImageBytes());
        response.getOutputStream().flush();
    }
}

//调用 request.getSession() 只是创建了 Session 对象并标记需要设置 Cookie，
// 真正的 Set-Cookie 响应头是在整个请求处理结束、响应即将发送时由容器自动生成的。