package com.fleetguard360.alert_management.service.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException forResource(String resourceName, String fieldName, Object value) {
        return new NotFoundException(String.format("%s no encontrado: %s=%s", resourceName, fieldName, value));
    }
}

