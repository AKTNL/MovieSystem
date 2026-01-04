package com.movie.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String userIdStr = request.getHeader("userId");
        String userRole = request.getHeader("userRole"); //前端把角色传过来

        // 如果是 OPTIONS 请求（预检请求），直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 权限校验
        if ("admin".equals(userRole)){
            return true; // 管理员放行
        }

        // 拒绝访问
        response.setStatus(403);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"code\":403, \"msg\":\"无权操作，需要管理员身份\"}");
        return false;
    }
}
