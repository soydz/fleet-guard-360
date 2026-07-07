package com.fleetguard360.presentation.dto;

public record AuthResDTO(
    String email,
    String message,
    String jwt,
    boolean status
) {}
