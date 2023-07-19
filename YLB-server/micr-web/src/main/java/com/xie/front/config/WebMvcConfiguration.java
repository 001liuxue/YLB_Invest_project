package com.xie.front.config;

import com.xie.front.interceptors.TokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtSecret);
        String [] path = {"/v1/user/realname"};
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(path);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
                //addMapping处理拦截地址
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  //可跨域的域名
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD")  //可跨域的方法
                .allowCredentials(true)  //是否允许浏览器发送cookie
                .maxAge(3600)  //设置预检请求的有效期
                .allowedHeaders("*");  //可跨域的请求头
    }
}
