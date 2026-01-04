package com.movie.entity;

import lombok.Data;

@Data
public class ActorVo {
    private Long actorId;
    private String name;
    private String avatarUrl;
    private String roleName; // 饰演的角色
}
