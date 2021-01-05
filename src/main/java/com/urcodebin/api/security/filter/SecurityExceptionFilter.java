package com.urcodebin.api.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urcodebin.api.error.handler.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class SecurityExceptionFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            LOGGER.error("Security Filter Error Occurred: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(LocalDateTime.now());
            errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.setMessage(e.getMessage());
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(convertObjectToJson(errorResponse));
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
