package com.fleetguard360.presentation.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiErrorResDTO(
    HttpStatus status,
    String path,
    String message,
    Instant timestamp
){}
