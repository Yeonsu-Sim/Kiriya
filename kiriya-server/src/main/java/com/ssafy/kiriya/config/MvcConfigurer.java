package com.ssafy.kiriya.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowCredentials(true);
		registry.addMapping("/s3/**")
	        .allowedOrigins("http://localhost:3000")
	        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
	        .allowCredentials(true);
    }
}
