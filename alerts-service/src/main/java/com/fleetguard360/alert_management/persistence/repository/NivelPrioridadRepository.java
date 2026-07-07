package com.fleetguard360.alert_management.persistence.repository;

import com.fleetguard360.alert_management.persistence.entity.NivelPrioridad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NivelPrioridadRepository extends JpaRepository<NivelPrioridad, Integer> {
    Optional<NivelPrioridad> findByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}

