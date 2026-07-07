package com.fleetguard360.presentation.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fleetguard360.presentation.dto.ApiErrorResDTO;
import com.fleetguard360.service.exception.InvalidCredentialsException;
import com.fleetguard360.service.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResDTO> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
       return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
               .body(new ApiErrorResDTO(
                       HttpStatus.UNAUTHORIZED,
                       request.getRequestURI(),
                       ex.getMessage(),
                       Instant.now()
                       ));
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResDTO> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ApiErrorResDTO(
                        HttpStatus.NOT_FOUND,
                        request.getRequestURI(),
                        ex.getMessage(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(value = JWTVerificationException.class)
    public ResponseEntity<ApiErrorResDTO> handlerJWTVerification(JWTVerificationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body( new ApiErrorResDTO(
                        HttpStatus.UNAUTHORIZED,
                        request.getRequestURI(),
                        ex.getMessage(),
                        Instant.now()
                        ));
    }
}
