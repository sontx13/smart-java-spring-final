package vn.project.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/api/v1/auth/**", "/storage/**",
                "/api/v1/apps/**",
                "/api/v1/companies/**", "/api/v1/jobs/**",
                "/api/v1/exams/**", "/api/v1/skills/**", "/api/v1/files",
                "/api/v1/resumes/**",
                "/api/v1/qas/**",
                "/api/v1/qas",
                "/api/v1/configs/**",
                "/api/v1/banners/**",
                "/api/v1/news/**",
                "/api/v1/categories/**",
                "/api/v1/infors/**",
                "/api/v1/hotlines/**",
                "/api/v1/zmaus/**",
                "/api/v1/zmaus",
                "/api/v1/subscribers/**"
        };
        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}
