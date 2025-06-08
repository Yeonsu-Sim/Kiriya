package com.ssafy.kiriya.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "KIRIYA API 명세서", description = "API 명세서 테스트 입니다.", version = "v1"))
public class SwaggerConfig {
	@Bean
	GroupedOpenApi attractionOpenApi() {
		String[] paths = { "/api/attractions/**" };
		return GroupedOpenApi.builder().group("Attraction 관련 API").pathsToMatch(paths).build();
	}
	
    @Bean
    GroupedOpenApi memberOpenApi() {
        String[] paths = { "/api/members/**" };
        return GroupedOpenApi.builder().group("Member 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi groupOpenApi() {
    	String[] paths = { "/api/groups/**" };
    	return GroupedOpenApi.builder().group("Group 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi TripOpenApi() {
    	String[] paths = { "/api/trips/**" };
    	return GroupedOpenApi.builder().group("Trip 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi PlanOpenApi() {
    	String[] paths = { "/api/plans/**" };
    	return GroupedOpenApi.builder().group("Plan 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi boardOpenApi() {
    	String[] paths = { "/api/boards/**" };
    	return GroupedOpenApi.builder().group("Board 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi noticeOpenApi() {
    	String[] paths = { "/api/notices/**" };
    	return GroupedOpenApi.builder().group("Notice 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi mapOpenApi() {
    	String[] paths = { "/api/map/**" };
    	return GroupedOpenApi.builder().group("Map 관련 API").pathsToMatch(paths).build();
    }

    @Bean
    GroupedOpenApi qnaOpenApi() {
    	String[] paths = { "/api/qnas/**" };
    	return GroupedOpenApi.builder().group("QnA 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi utilOpenApi() {
    	String[] paths = { "/api/util/**" };
    	return GroupedOpenApi.builder().group("Util 관련 API").pathsToMatch(paths).build();
    }
    
    @Bean
    GroupedOpenApi otherOpenApi() {
        String[] paths = { "/api/etc/**" };
        return GroupedOpenApi.builder().group("기타 API").pathsToMatch(paths).build();
    }
}
