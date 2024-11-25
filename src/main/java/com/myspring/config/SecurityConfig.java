package com.myspring.config;

import com.myspring.filter.JwtAuthFilter;
import com.myspring.service.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImp userDetailsServiceImp;
    private final JwtAuthFilter authFilter;

    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp, JwtAuthFilter authFilter) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.authFilter = authFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/api/v1/auth/**", "/demo").permitAll()
                                .requestMatchers("/admin").hasAuthority("ADMIN")
                                .requestMatchers("/user").hasAuthority("USER")
                                .requestMatchers("/user_admin").hasAnyAuthority("USER", "ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .userDetailsService(userDetailsServiceImp)
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
