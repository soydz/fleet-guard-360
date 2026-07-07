package com.fleetguard360.alert_management.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un catálogo de los niveles de
 * prioridad o severidad para las alertas.
 */
@Entity
@Table(name = "nivel_prioridad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NivelPrioridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}
