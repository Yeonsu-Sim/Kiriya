package com.ssafy.kiriya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ssafy.kiriya.model.service.member.MemberService;
import com.ssafy.kiriya.security.filter.JWTAuthenticationFilter;
import com.ssafy.kiriya.security.filter.JWTVerificationFilter;
import com.ssafy.kiriya.security.service.CustomUserDetailService;
import com.ssafy.kiriya.security.util.jwt.JWTUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService userDetailsService;
    private final JWTUtil                jwtUtil;
    private final CorsConfigurationSource corsConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authManager,
                                                         JWTUtil jwtUtil,
                                                         @Lazy MemberService memberService) {
        return new JWTAuthenticationFilter(authManager, jwtUtil, memberService);
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, JWTAuthenticationFilter authFilter) throws Exception {
        // 1) AuthenticationManager 직접 가져오기
//        AuthenticationManager authManager = authenticationManager(
//            http.getSharedObject(AuthenticationConfiguration.class)
//        );

        // 2) 필터를 @Component가 아닌 여기서 new
//        JWTAuthenticationFilter authFilter =
//            new JWTAuthenticationFilter(authManager, jwtUtil, memberService);
        authFilter.setFilterProcessesUrl("/api/members/login");
        authFilter.setUsernameParameter("email");
        authFilter.setPasswordParameter("password");

        JWTVerificationFilter verifyFilter =
            new JWTVerificationFilter(jwtUtil, userDetailsService);

        http
          .securityMatcher("/api/**")
          .cors(cors -> cors.configurationSource(corsConfig))
          .csrf(csrf -> csrf.disable())
          .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

          // 3) AuthenticationProvider 등록
          .authenticationProvider(daoAuthProvider())

          // 4) 인가 설정
          .authorizeHttpRequests(auth -> auth
              .requestMatchers(HttpMethod.POST,
                  "/api/members/signup",
                  "/api/members/login",
                  "/api/members/password/find").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/members/exist").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/util/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/attractions/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/boards/**").permitAll()
              
              // 임시 접근 허용
              .requestMatchers(HttpMethod.GET, "/api/members/*/attractions/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/members/*/trips").permitAll()
              .requestMatchers(HttpMethod.POST, "/api/attractions/*/likes/**").permitAll()
              .requestMatchers(HttpMethod.DELETE, "/api/attractions/*/likes/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/api/map/**").permitAll()
              .requestMatchers("/api/trips/**").permitAll()
              .requestMatchers("/api/plans/**").permitAll()              

              .requestMatchers(HttpMethod.POST, "/s3/upload").permitAll()
              .requestMatchers(HttpMethod.POST, "/api/boards").permitAll()
              
              .requestMatchers(HttpMethod.POST, "/api/members/logout").authenticated()
              .requestMatchers("/api/members/{mno}/**").authenticated()
              .requestMatchers("/api/groups/**").authenticated()
              .anyRequest().authenticated()
          )

          // 5) 필터 체인에 등록
          .addFilterBefore(verifyFilter, UsernamePasswordAuthenticationFilter.class)
          .addFilterAt   (authFilter,   UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


