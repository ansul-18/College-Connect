package com.cllg.chat_service.security;

import com.cllg.chat_service.dto.request.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(
            JwtService jwtService
    ) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization =
                request.getHeader("Authorization");

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String token =
                authorization.substring(7);

        try {

            AuthenticatedUser user =
                    jwtService.authenticate(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user.userId(),
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" +
                                            user.role()
                                    )
                            )
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(
                            authentication
                    );
            System.out.println("========== JWT AUTH SUCCESS ==========");
            System.out.println("USER ID = " + user.userId());
            System.out.println("ROLE = " + user.role());

        } catch (Exception exception) {

            SecurityContextHolder.clearContext();
            System.out.println("========== JWT AUTH FAILED ==========");
            System.out.println("URI = " + request.getRequestURI());
            System.out.println("ERROR = " + exception.getClass().getName());
            System.out.println("MESSAGE = " + exception.getMessage());
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}