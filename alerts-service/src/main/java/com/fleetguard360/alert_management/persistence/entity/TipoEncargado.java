package com.fleetguard360.alert_management.persistence.entity;

/**
 * Enum que define los tipos de encargados responsables
 * de atender las diferentes alertas del sistema.
 */
public enum TipoEncargado {
    CONDUCTOR("Conductor"),
    MECANICO("Mecánico"),
    SOPORTE_TECNICO("Soporte Técnico"),
    OPERADOR_LOGISTICA("Operador de Logística"),
    SEGURIDAD("Seguridad");

    private final String descripcion;

    TipoEncargado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
