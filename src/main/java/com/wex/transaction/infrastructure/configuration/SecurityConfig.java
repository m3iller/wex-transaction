package com.wex.transaction.infrastructure.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApiKeyAuthFilter apiKeyAuthFilter;

    public SecurityConfig(ApiKeyAuthFilter apiKeyAuthFilter) {
        this.apiKeyAuthFilter = apiKeyAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());

        return http.build();
    }

    @Component
    public static class ApiKeyAuthFilter extends OncePerRequestFilter {

        @Value("${api.security.key}")
        private String apiKey;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            // Define public paths that should bypass the API key check
            String path = request.getRequestURI();
            if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
                filterChain.doFilter(request, response);
                return;
            }

            // For all other requests, validate the API key
            String requestApiKey = request.getHeader("X-API-KEY");

            if (apiKey.equals(requestApiKey)) {
                // Key is valid, proceed with the request
                filterChain.doFilter(request, response);
            } else {
                // Key is missing or invalid, return 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Missing or invalid API Key");
            }
        }
    }
}



















