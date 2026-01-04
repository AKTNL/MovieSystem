package com.movie.config;

import com.movie.common.AdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // 注册管理员拦截器
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/movie/add", "/movie/update", "/movie/delete"); //拦截电影管理接口
    }
}
