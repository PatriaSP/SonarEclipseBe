package com.patria.apps.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patria.apps.exception.ExceptionResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class InputSanitizationFilter extends OncePerRequestFilter {

    private static final Pattern XSS_PATTERN = Pattern.compile(
            "(?i)(<script.*?>.*?</script>|"
            + "javascript:|"
            + "on\\w+=|"
            + "%3Cscript%3E|"
            + "eval\\(|"
            + "expression\\(|"
            + "document\\.cookie|"
            + "document\\.write|"
            + "window\\.location|"
            + "window\\.onload)",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        
        if ((request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart/")) || path.contains("collaboration")) {
            filterChain.doFilter(request, response);
            return;
        }

        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        if (checkXSSAttack(wrappedRequest) || checkXSSInBody(wrappedRequest)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            ExceptionResponse exceptionResponse = new ExceptionResponse();
            exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST);
            exceptionResponse.setMessage("Input data is prohibited!");
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
            return;
        }
        filterChain.doFilter(wrappedRequest, response);
    }

    //check xss attack
    private boolean checkXSSAttack(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            if (XSS_PATTERN.matcher(paramValue).find()) {
                return true;
            }
        }
        return false;
    }

    //check attack on body request
    private boolean checkXSSInBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        reader.close();
        String bodyContent = body.toString().replaceAll("\\s+", "");
        return !isBase64Encoded(bodyContent) && XSS_PATTERN.matcher(bodyContent).find();
    }

    //exclude base64 encoded like aes encryption
    private boolean isBase64Encoded(String value) {
        try {
            byte[] decoded = Base64.getDecoder().decode(value);
            return decoded.length > 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
