package com.app.lika.security;

import com.app.lika.exception.ExceptionCustomCode;
import com.app.lika.payload.response.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println(authException.getClass());
        LOGGER.error("Responding with unauthorized error.Messsage - {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        APIResponse body = new APIResponse();

        if (authException.getClass().isAssignableFrom(LockedException.class)) {
            body.setErrorCode(ExceptionCustomCode.ACCOUNT_LOCKED.getCode());
            body.setMessage("This account has not been activated");
        } else if (authException.getClass().isAssignableFrom(InternalAuthenticationServiceException.class)) {
            body.setErrorCode(ExceptionCustomCode.USERNAME_NOT_FOUND.getCode());
            body.setMessage("This username was not found");
        } else if (authException.getClass().isAssignableFrom(BadCredentialsException.class)) {
            body.setErrorCode(ExceptionCustomCode.INCORRECT_PASSWORD.getCode());
            body.setMessage("The password is incorrect");
        } else if (request.getAttribute("expiredJwt") != null) {
            body.setErrorCode(ExceptionCustomCode.JWT_TOKEN_EXPIRED.getCode());
            body.setMessage("JWT token is expired");
        } else {
            body.setMessage(authException.getMessage());
            body.setErrorCode(HttpStatus.UNAUTHORIZED.value());
        }

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
