package com.card_management.web_security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtParser jwtParser;
    UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtParser jwtParser, UserDetailsService userDetailsService) {
        this.jwtParser = jwtParser;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ") &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                token = token.substring(7);

                var tokenData = jwtParser.parse(token);

                if (!tokenData.isExpired()) {
                    var details = new SecurityUser(tokenData.role(), token, tokenData.email(), tokenData.id());

                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                            details,
                            null,
                            details.getAuthorities()));
                }
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }


        doFilter(request, response, filterChain);
    }
}
