package com.patria.apps.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTRequestFilterComponent jwtRequestFilterComponent;
    private final InputSanitizationFilter inputSanitizationFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                .requestMatchers("/v1/api/auth/**", "/v1/swagger-ui/**", "/v1/docs/**")
                .permitAll()
                .anyRequest().authenticated())
                .addFilterBefore(inputSanitizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRequestFilterComponent, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
