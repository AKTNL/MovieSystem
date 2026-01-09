package com.movie.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.movie.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/image")
    public Result<Map<String, String>> getCaptcha(){
        // 1. 生成验证码图片 (宽100, 高40, 4个字符, 干扰线10条)
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 40, 4, 10);

        // 2. 获取验证码里的文字
        String code = lineCaptcha.getCode();

        // 3. 生成一个唯一标识 UUID
        String uuid = UUID.randomUUID().toString();

        // 4. 存入 Redis: Key=uuid, Value=code, 过期时间=2分钟
        redisTemplate.opsForValue().set("captcha:" + uuid, code, 2, TimeUnit.MINUTES );

        // 5. 返回给前端: UUID 和 图片的 Base64 字符串
        Map<String, String> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("image", lineCaptcha.getImageBase64Data());

        return Result.success(map);
    }
}