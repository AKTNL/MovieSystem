package com.movie.common;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthAspect {
    // @Around 环绕通知：在目标方法执行前后都能做事
    // "@annotation(auth)" 表示拦截所有带 @Auth 注解的方法，并把注解实例注入进来
    @Around("@annotation(auth)")
    public Object doAround(ProceedingJoinPoint joinPoint, Auth auth) throws Throwable {
        // 1. 获取当前的 Request 对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 2. 获取前端传来的角色信息
        String userRole = request.getHeader("userRole");

        // 3. 获取注解上要求的角色
        String requiredRole = auth.value();

        // 4. 鉴权逻辑
        if("admin".equals(requiredRole) && !"admin".equals(userRole)){
            throw new RuntimeException("无权访问，需要管理员身份");
        }

        // 5. 验证通过，放行，执行原来的 Controller 方法
        return joinPoint.proceed();
    }
}
