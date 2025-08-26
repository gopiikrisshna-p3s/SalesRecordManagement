package com.example.SalesRecordManagement.Config;

import com.example.SalesRecordManagement.Handler.CustomAccessDeniedHandler;
import com.example.SalesRecordManagement.Handler.CustomAuthenticationEntryPoint;
import com.example.SalesRecordManagement.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**","/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/analyst/**").hasAnyRole("ANALYST","ADMIN")
                        .requestMatchers("/viewer/**").hasAnyRole("VIEWER","ADMIN")
                        .requestMatchers("/customer/**").hasAnyRole("CUSTOMER","ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .build();
    }
}