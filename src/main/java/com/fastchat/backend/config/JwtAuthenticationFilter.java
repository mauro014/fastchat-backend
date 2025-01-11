package com.fastchat.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    // This method is executed for every request intercepted by the filter.
    //And, it extract the token from the request header and validate the token.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get JWT token from HTTP request
        String token = getTokenFromRequest(request);

        // Validate Token
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){

            String email = jwtTokenProvider.getEmailFromToken(token);

            // Create a simple Authentication object (no roles)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, null);

            // Set the authentication in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Extract the token
    private String getTokenFromRequest(HttpServletRequest request){

        try {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt")) {
                    return cookie.getValue();
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }
}