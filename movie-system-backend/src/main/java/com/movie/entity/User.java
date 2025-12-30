package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String username;
    private String password; //存储加密后的密码
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private Integer status; //0:禁用 1:正常
    private LocalDateTime createTime;
}
