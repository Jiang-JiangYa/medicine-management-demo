package org.example.myspringboot.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 功能：拦截器配置，配置要拦截的路径，应用配置的拦截器等
 * 作者：yangjia
 * 日期：2024/8/20 上午11:29
 */
@Configuration     //表示这是配置类
public class InterceptorConfig extends WebMvcConfigurationSupport {

     @Override
    protected void addInterceptors(InterceptorRegistry registry) {
         //addInterceptors - 添加拦截器方法，  注册一个自定义的拦截器jwtInterceptor
         //InterceptorRegistry  注册拦截器类
        registry.addInterceptor(jwtInterceptor())  //表示将以下配置放入这个自定义jwt拦截器中（JwtInterceptor类）
                .addPathPatterns("/**")     //表示拦截所有路径
                .excludePathPatterns("/user/login");   //允许登录接口发送请求，未登录没有token

        super.addInterceptors(registry);
    }

    @Bean   //注入Spring
    public JwtInterceptor jwtInterceptor() {
         return new JwtInterceptor();
    }
}





























