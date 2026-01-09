package com.movie.entity;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    private String code; // 用户输入的验证码
    private String uuid; // 验证码的唯一标识
}
