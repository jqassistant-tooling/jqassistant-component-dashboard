package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.auth.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class ApiSecurityConfiguration {

    @Order(0)
    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http, RestApiProperties restApiProperties) {
        if (restApiProperties.getAuthToken() == null) {
            log.warn("No authentication token configured, REST-API will not be available.");
        }
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(restApiProperties);
        HttpSecurity httpSecurity = http.securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest()
                .authenticated())
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
