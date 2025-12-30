package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("director")
public class Director {
    @TableId(type = IdType.AUTO)
    private Long directorId;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String nationality;
    private String avatarUrl;
}
