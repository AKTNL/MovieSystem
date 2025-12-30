package com.movie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movie.entity.User;

public interface UserService extends IService<User> {
    void register(User user);
    User login(String username, String password);
}
