package com.proksi.assessment.component;

import com.proksi.assessment.exception.JwtExceptionHandler;
import com.proksi.assessment.exception.TokenRequiredException;
import com.proksi.assessment.service.ApiUserService;
import com.proksi.assessment.service.JwtUtilService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtilService jwtUtilService;
    private final ApiUserService userService;
    private final JwtExceptionHandler exceptionHandler;

    @Autowired
    public JwtRequestFilter(JwtUtilService jwtUtilService, ApiUserService userService, JwtExceptionHandler exceptionHandler) {
        this.jwtUtilService = jwtUtilService;
        this.userService = userService;
        this.exceptionHandler = exceptionHandler;
    }

    private String resolveToken(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new JwtException("JWT Token is required.");
        }
        return authorizationHeader.substring(7);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/auth/login")
                || path.equals("/api-user/sign-up")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-resources")
                || path.equals("/swagger-ui.html");
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            Claims claims = jwtUtilService.parseToken(token);
            String email = claims.getSubject();
            Date expirationDate = claims.getExpiration();

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(email);
                if (jwtUtilService.validateToken(email, expirationDate, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new JwtException("Token is required.");
                }
            }
            filterChain.doFilter(request, response);
        } catch(Exception e) {
            exceptionHandler.handelJwtException(response);
        }
    }
}
