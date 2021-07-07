package top.magicdevil.example.webapp.sample.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import top.magicdevil.example.webapp.sample.bookstore.interceptor.AdminLoginInterceptor;
import top.magicdevil.example.webapp.sample.bookstore.interceptor.LoginInterceptor;
import top.magicdevil.example.webapp.sample.bookstore.interceptor.RecommendationInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public AdminLoginInterceptor adminLoginInterceptor() {
        return new AdminLoginInterceptor();
    }

    @Bean
    public RecommendationInterceptor recommendationInterceptor() {
        return new RecommendationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor())
                .addPathPatterns(
                        "/user/**",
                        "/admin/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/js/**",
                        "/css/**",
                        "/img/**",
                        "/font/**",
                        "/icon/**");

        registry.addInterceptor(adminLoginInterceptor())
                .addPathPatterns("/admin/**");

        registry.addInterceptor(recommendationInterceptor())
                .addPathPatterns(
                        "/user/shop/**",
                        "/user/order/pay")
                .excludePathPatterns("/user/shop/list");

    }

}
