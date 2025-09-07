package com.patria.apps.config;

import com.patria.apps.helper.JWTHelperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patria.apps.entity.Users;
import com.patria.apps.exception.ExceptionResponse;
import com.patria.apps.users.service.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTRequestFilterComponent extends OncePerRequestFilter {

    private final JWTHelperService jwtHelperService;
    private final UsersService usersService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/v1/api/auth/login")
            || path.startsWith("/v1/swagger-ui")
            || path.startsWith("/v1/docs");
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null) {
            String token = authHeader.replace("Bearer ", "");

            Optional<Users> user = jwtHelperService.getUserByToken(token, request);

            if (user == null || user.isEmpty() || !user.map(Users::getIsActive).orElse(true)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                ExceptionResponse exceptionResponse = new ExceptionResponse();
                exceptionResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                exceptionResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                exceptionResponse.setMessage("Not Authorized");
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
                return;
            }

            UserDetails userDetails = usersService.loadUserByUsername(user.get().getUsername());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
            authenticationToken.setDetails(webAuthenticationDetails);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);

        }

        filterChain.doFilter(request, response);
    }

}
