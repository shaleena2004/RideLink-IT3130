package com.ridelink.driver_vehicle_service.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        System.out.println("AUTH HEADER = " + authorizationHeader);

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            System.out.println("NO TOKEN FOUND");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        System.out.println("TOKEN RECEIVED = " + token);

        try {

            if (jwtService.isTokenValid(token)) {

                System.out.println("TOKEN VALID");

                String userId =
                        jwtService.extractUserId(token);

                String role =
                        jwtService.extractRole(token);

                System.out.println("USER ID = " + userId);
                System.out.println("ROLE = " + role);

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + role);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                List.of(authority)
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                System.out.println("AUTHENTICATION SET");
            } else {

                System.out.println("TOKEN INVALID");
            }

        } catch (Exception exception) {

            System.out.println("JWT ERROR:");
            exception.printStackTrace();

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
