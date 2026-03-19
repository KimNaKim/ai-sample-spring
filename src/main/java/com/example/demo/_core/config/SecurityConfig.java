package com.example.demo._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (테스트 편의성)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 모든 요청 허용 (현재의 수동 로그인 유지)
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // H2 콘솔 사용을 위해 설정
        
        return http.build();
    }
}
