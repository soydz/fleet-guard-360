package com.fleetguard360.panel_service.persistence.repository;

import com.fleetguard360.panel_service.persistence.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
}

