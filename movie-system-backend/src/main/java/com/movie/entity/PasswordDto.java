package com.movie.entity;

import lombok.Data;

@Data
public class PasswordDto {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}
