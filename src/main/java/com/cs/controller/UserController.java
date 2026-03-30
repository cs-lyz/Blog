package com.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cs.dto.HttpStatusEnum;
import com.cs.dto.LoginForm;
import com.cs.dto.Result;
import com.cs.entity.AdminUser;
import com.cs.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    //  登录
    @RequestMapping("/login")
    public Result<String> login(@RequestBody LoginForm form, HttpSession session){
        // 1. 参数校验
        if (StringUtils.isBlank(form.getCode()) ||
                StringUtils.isBlank(form.getLoginUserName()) ||
                StringUtils.isBlank(form.getLoginPassword())) {
            return Result.fail(HttpStatusEnum.BAD_REQUEST.getCode(), HttpStatusEnum.BAD_REQUEST.getMessage());
        }
        // 2. session验证码校验
        Object captcha = session.getAttribute("captcha");
        if (captcha == null || !form.getCode().equalsIgnoreCase(captcha.toString())) {
            return Result.fail(HttpStatusEnum.BAD_REQUEST.getCode(), "验证码过期");
        }
        session.removeAttribute("captcha"); // 一次性使用
        if(!form.getCode().equals(session.getAttribute("captcha")) ){
            return Result.fail(HttpStatusEnum.BAD_REQUEST.getCode(), "验证码错误");
        }
        //ok,查数据库了
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<AdminUser>();
        wrapper.eq(AdminUser::getLoginUserName,form.getLoginUserName());
        AdminUser adminUser=userMapper.selectOne(wrapper);

        if(adminUser==null){
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(), "用户名或密码错误");
        }
        if(adminUser.getLocked()==1){
            return Result.fail(HttpStatusEnum.USER_LOCKED.getCode(), "用户被封禁");
        }
        if(!adminUser.getLoginPassword().equals(form.getLoginPassword())){
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(), "用户名或密码错误");
        }
        //ok 三项都过了
        session.setAttribute("adminUserId",adminUser.getAdminUserId());
        session.setAttribute("loginUserName",adminUser.getLoginUserName());

        return Result.success("login success",null);
    }

    @RequestMapping("/password")
    public Result<String> changePassword(@RequestParam String oldPassword,@RequestParam String newPassword, HttpSession session){
        if (StringUtils.isBlank(newPassword) || StringUtils.isBlank(oldPassword)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(), "密码不能为空");
        }
        if (oldPassword.equals(newPassword)) {
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(),"新旧密码不能相同");
        }
        //获取登录用户id  一定有，没有到不了这一步
        Integer id = (Integer) session.getAttribute("adminUserId");
        AdminUser adminUser = userMapper.selectById(id);
        if (adminUser == null) {
            return Result.fail(HttpStatusEnum.BUSINESS_ERROR.getCode(), "用户不存在");
        }
        if(!adminUser.getLoginPassword().equals(oldPassword)){
            return Result.fail(HttpStatusEnum.PARAM_ERROR.getCode(), "原密码错误");
        }
        //更新密码
        LambdaUpdateWrapper<AdminUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AdminUser::getAdminUserId,id)
                        .set(AdminUser::getLoginPassword,newPassword);
        if(userMapper.update(null,wrapper)==0){
            return Result.fail(HttpStatusEnum.BUSINESS_ERROR.getCode(), "修改失败");
        }
        return Result.success("change password success",null);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        if (session != null) {
            session.invalidate();   // 彻底销毁会话
        }
        return Result.success("logout success",null);
    }
}


//    Spring MVC 在调用这个方法前，会从当前的 HttpServletRequest 中获取 HttpSession 对象。
//  获取逻辑等价于：request.getSession()（如果会话不存在，则自动创建）。
//  然后将这个对象作为参数传入方法，供你操作会话属性。
//  同一个用户发起的多个请求，即使由不同的线程处理，这些线程都能访问到同一个 Session 对象，因此调用 setAttribute 后，该用户后续请求都能读取到新设置的值。