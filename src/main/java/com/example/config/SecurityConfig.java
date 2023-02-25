package com.example.config;

import com.example.filter.JwtAuthorizationFilter;
import com.example.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final JwtUtil jwtUtil;

    public SecurityConfig(CorsConfig corsConfig, JwtUtil jwtUtil) {

        this.corsConfig = corsConfig;
        this.jwtUtil = jwtUtil;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return
            http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .apply(new MyCustomDsl()) // 커스텀 필터 등록
                .and()
                .authorizeRequests()
                .antMatchers("/api/users/**").permitAll()
                .antMatchers("/api/todo/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .build();

    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(
                AuthenticationManager.class);
            http
                .addFilter(corsConfig.corsFilter())
                .addFilter(
                    new JwtAuthorizationFilter(authenticationManager,  jwtUtil));
        }
    }

}
