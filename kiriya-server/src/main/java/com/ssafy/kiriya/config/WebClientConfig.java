package com.ssafy.kiriya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	/* API Keys */
    @Value("${api.key_kakao_rest}")
    private String keyKakaoRest;
    
	@Bean
	WebClient kakaoNaviClient() {
	    return WebClient.builder()
	            .baseUrl("https://apis-navi.kakaomobility.com")
	            .defaultHeader("Authorization", "KakaoAK "+keyKakaoRest)
	            .build();
	}
}
