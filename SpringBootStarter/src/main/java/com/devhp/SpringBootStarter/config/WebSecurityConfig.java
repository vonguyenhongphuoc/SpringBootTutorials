package com.devhp.SpringBootStarter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {
    private static final String[] WHITELIST = {
            "/",
            "/login",
            "/register",
            "/db-console/**",
            "/css/**",
            "/fonts/**",
            "/images/**",
            "/js/**",
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(WHITELIST).permitAll()
                .anyRequest().authenticated());

        // Todo: remove these after upgrading the DB from H2 in file DB
        // Sẽ tắt chức năng bảo vệ chống tấn công CSRF
        http.csrf((csrf) -> csrf.disable());
        // Sẽ tắt chức năng bảo vệ khung (Frame). Điều này cho phép trang web có thể
        // được nhúng vào một khung (Frame) hoặc iframe.
        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()));

        return http.build();
    }

}
