package com.fleetguard360.panel_service.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alert_type", nullable = false)
    private String alertType;

    @Column(name = "responsible", nullable = false)
    private String responsible;

    @Column(name = "priority", nullable = false)
    private String priority;

    @Column(name = "driver", nullable = false)
    private String driver;

    @Column(name = "generating_unit", nullable = false)
    private String generatingUnit;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "generation_date", nullable = false)
    private LocalDateTime generationDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

