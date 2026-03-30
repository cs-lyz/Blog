package com.cs.config;

import com.cs.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //先添加的先执行
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/captcha",   // 排除登录接口
                                    "/blog/**",//测试
                                    "/user/login");
    }
}