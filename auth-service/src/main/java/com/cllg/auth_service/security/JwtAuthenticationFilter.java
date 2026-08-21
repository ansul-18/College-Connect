package com.cllg.auth_service.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1. Get Authorization header
        String authHeader = request.getHeader("Authorization");

        //2. No JWT → continue request
        if (authHeader==null||!authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        //3. Remove "Bearer "
        String token = authHeader.substring(7);


        try {
            // 4. Extract email from JWT
            String email = jwtService.extractUsername(token);

            //5. Check if user is already authenticated
            if (email!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
                //6. Load user from database
                UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(email);

                //7. Validate JWT
                if (jwtService.isTokenValid(token, userDetails)) {
                    //8. Create authenticated object
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    //9. Add request details
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //10. Set authentication in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid Token : "+e.getMessage());
        }
        //11. Continue filter chain
        filterChain.doFilter(request, response);
    }
}
