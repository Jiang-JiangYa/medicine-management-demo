package org.example.myspringboot.controller;

import cn.hutool.core.util.StrUtil;
import org.example.myspringboot.common.AuthAccess;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.entity.User;
import org.example.myspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能：登录、注册相关的接口处理
 * 作者：yangjia
 * 日期：2024/8/16 下午5:29
 */

@RestController
@RequestMapping("/user")
public class LoginRegisterController {

    @Autowired
    UserService userService;

//    @GetMapping("/")
//    public Result hello() {
//        return Result.success("hello");
//    }

    @PostMapping("/login")
    public  Result login(@RequestBody User user, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        //hutool工具检验数据是否为空
        if(StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法！");
        }
        User loggedIn = userService.login(user);
        org.springframework.http.ResponseCookie cookie = org.springframework.http.ResponseCookie.from("demo_session", loggedIn.getToken())
                .httpOnly(true).secure(request.isSecure()).sameSite("Strict").path("/api").maxAge(7200).build();
        response.addHeader("Set-Cookie", cookie.toString());
        return Result.success(loggedIn);
    }

    @PostMapping("/logout")
    public Result logout(javax.servlet.http.HttpServletResponse response) {
        response.addHeader("Set-Cookie", "demo_session=; Path=/api; HttpOnly; SameSite=Strict; Max-Age=0");
        return Result.success();
    }

    @PostMapping("/register")
    public  Result register(@RequestBody User user ) {
        //hutool工具检验数据是否为空
        if(StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法！");
        }
        userService.register(user);
        return Result.success();
    }

    /**
     * 忘记密码，--> 重置密码
     */
    @AuthAccess
    @PutMapping("/password")
    public  Result password(@RequestBody User user ) {
        // 校验 username，phone 是否为空
        if(StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPhone())) {
            return Result.error("数据输入不合法！");
        }
        userService.resetPassword(user);
        return Result.success();
    }
}

















