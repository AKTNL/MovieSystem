package com.movie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.common.Auth;
import com.movie.common.Result;
import com.movie.entity.LoginDto;
import com.movie.entity.PasswordDto;
import com.movie.entity.Review;
import com.movie.entity.User;
import com.movie.service.ReviewService;
import com.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ReviewService reviewService;

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
    public Result<Map<String, Object>> getUserInfo(@PathVariable Long id) {
        // 1. 查用户基础信息
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null); // 隐藏密码

        // 2. 查统计数据
        // 2.1 查该用户的所有评论
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        List<Review> reviews = reviewService.list(queryWrapper);

        // 2.2 计算数量
        int reviewCount = reviews.size();

        // 2.3 计算平均给分
        double avgScore = 0.0;
        if (reviewCount > 0) {
            // 求和
            double sum = reviews.stream().mapToInt(Review::getScore).sum();
            // 计算平均值 (保留1位小数)
            avgScore = Math.round((sum / reviewCount) * 10.0) / 10.0;
        }

        // 3. 封装返回结果
        Map<String, Object> map = new HashMap<>();

        map.put("userId", user.getUserId());
        map.put("username", user.getUsername());
        map.put("nickname", user.getNickname());
        map.put("avatar", user.getAvatar());
        map.put("email", user.getEmail());
        map.put("phone", user.getPhone());
        map.put("role", user.getRole());
        map.put("bio", user.getBio());

        // 放入统计数据
        map.put("reviewCount", reviewCount);
        map.put("avgScore", avgScore);
        map.put("watchedCount", reviewCount); // 暂时用评论数代替看过数

        return Result.success(map);
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

    //获取所有用户
    @Auth("admin")
    @GetMapping("/list")
    public Result<List<User>> list(){
        return Result.success(userService.list());
    }

    //删除用户
    @Auth("admin")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        userService.removeById(id);
        return Result.success(null);
    }
}
