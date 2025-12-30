package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.User;
import com.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //注册接口
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        try{
            userService.register(user);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //登录接口
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user){
        try{
            User loginUser = userService.login(user.getUsername(), user.getPassword());
            return Result.success(loginUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
