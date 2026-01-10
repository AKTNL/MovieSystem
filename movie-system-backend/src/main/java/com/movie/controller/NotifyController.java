package com.movie.controller;

import com.movie.common.Auth;
import com.movie.common.Result;
import com.movie.websocket.WebSocketServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class NotifyController {
    // 管理员群发消息
    @Auth("admin")
    @PostMapping("/all")
    public Result<?> sendToAll(@RequestParam String msg) {
        WebSocketServer.sendToAll(msg);
        return Result.success("群发成功");
    }

    // 管理员给指定人发消息
    @Auth("admin")
    @PostMapping("/user")
    public Result<?> sendToUser(@RequestParam String userId, @RequestParam String msg) {
        WebSocketServer.sendInfo(msg, userId);
        return Result.success("发送成功");
    }
}
