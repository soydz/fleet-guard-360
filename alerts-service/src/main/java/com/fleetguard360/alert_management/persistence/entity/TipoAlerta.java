package com.fleetguard360.alert_management.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un catálogo de los tipos de alerta
 * que pueden ser configurados en el sistema.
 */
@Entity
@Table(name = "tipo_alerta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoAlerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Relación Muchos a Uno: Muchos tipos de alerta pueden tener el mismo Nivel de Prioridad.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nivel_prioridad_id", nullable = false)
    private NivelPrioridad nivelPrioridad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_encargado", nullable = false, length = 50)
    private TipoEncargado tipoEncargado;
}
