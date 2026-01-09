package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.LoginDto;
import com.movie.entity.User;
import com.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

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
    public Result<User> login(@RequestBody LoginDto loginDto){
        //校验验证码
        String uuid = loginDto.getUuid();
        String userCode = loginDto.getCode();

        if(uuid == null || userCode == null){
            return Result.error("验证码失效");
        }

        //从 Redis 取出真实验证码
        String realCode = redisTemplate.opsForValue().get("captcha:" + uuid);

        if (realCode == null){
            return Result.error("验证码已过期，请刷新");
        }

        // 忽略大小写比对
        if (!realCode.equalsIgnoreCase(userCode)){
            return Result.error("验证码错误");
        }

        // 验证通过，删除 Redis 里的这个码
        redisTemplate.delete("captcha:" + uuid);

        try{
            User loginUser = userService.login(loginDto.getUsername(), loginDto.getPassword());
            return Result.success(loginUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
