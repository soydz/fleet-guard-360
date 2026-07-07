package com.fleetguard360.alert_management.persistence.repository;

import com.fleetguard360.alert_management.persistence.entity.TipoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoAlertaRepository extends JpaRepository<TipoAlerta, Integer> {
    Optional<TipoAlerta> findByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}

