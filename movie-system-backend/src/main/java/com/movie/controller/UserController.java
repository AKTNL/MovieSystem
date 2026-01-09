package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.LoginDto;
import com.movie.entity.PasswordDto;
import com.movie.entity.User;
import com.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update")
    public Result<?> update(@RequestBody User user){
        //用户不能修改自己的username和role
        user.setUsername(null);
        user.setRole(null);
        user.setPassword(null);
        userService.updateById(user);
        return Result.success(null);
    }

    //根据Token(或ID)获取最新用户信息
    @GetMapping("/{id}")
    public Result<User> getInfo(@PathVariable Long id){
        User user = userService.getById(id);
        user.setPassword(null); //敏感信息不返回
        return Result.success(user);
    }

    @PostMapping("/password")
    public Result<?> updatePassword(@RequestBody PasswordDto passwordDto) {
        try{
            userService.updatePassword(passwordDto);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
