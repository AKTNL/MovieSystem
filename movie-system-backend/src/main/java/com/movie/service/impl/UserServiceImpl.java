package com.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.entity.PasswordDto;
import com.movie.entity.User;
import com.movie.mapper.UserMapper;
import com.movie.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public void register(User user){
        //1.检查用户名是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        if(count(queryWrapper)>0){
            throw new RuntimeException("用户名已存在");
        }

        //2.密码加密（MD5加密）
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);

        //3.设置默认值
        user.setNickname("用户" + System.currentTimeMillis());//默认昵称
        user.setRole("user");
        user.setStatus(1);

        //4.保存
        save(user);
    }

    @Override
    public User login(String username, String password){
        //1.根据用户名查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = getOne(queryWrapper);

        //2.用户不存在
        if(user == null){
            throw new RuntimeException("用户不存在");
        }

        //3.校验密码
        String md5Input = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!md5Input.equals(user.getPassword())){
            throw new RuntimeException("密码错误");
        }

        //4.登录成功，返回用户信息
        user.setPassword(null);
        return user;
    }

    @Override
    public void updatePassword(PasswordDto passwordDto){
        //1.查用户
        User user = getById(passwordDto.getUserId());
        if(user == null){
            throw new RuntimeException("用户不存在");
        }

        //2.校验旧密码
        String md5Input = DigestUtils.md5DigestAsHex(passwordDto.getOldPassword().getBytes());
        if(!md5Input.equals(user.getPassword())){
            throw new RuntimeException("旧密码错误");
        }

        //3.设置新密码
        String md5NewPassword = DigestUtils.md5DigestAsHex(passwordDto.getNewPassword().getBytes());
        user.setPassword(md5NewPassword);

        updateById(user);
    }
}
