package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.auth;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.config.RestApiProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends GenericFilterBean {

    public static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    private final RequestMatcher requestMatcher;

    private final RestApiProperties restApiProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpServletRequest && requestMatcher.matches(httpServletRequest)) {
            String apiKey = httpServletRequest.getHeader(AUTH_TOKEN_HEADER_NAME);
            if (apiKey == null || !apiKey.equals(restApiProperties.getAuthToken())) {
                throw new BadCredentialsException("Invalid API Key");
            }
            Authentication authentication = new ApiTokenAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
            SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
