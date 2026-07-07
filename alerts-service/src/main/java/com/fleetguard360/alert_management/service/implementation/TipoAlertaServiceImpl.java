package com.fleetguard360.alert_management.service.implementation;

import com.fleetguard360.alert_management.configuration.mapper.TipoAlertaMapper;
import com.fleetguard360.alert_management.persistence.entity.NivelPrioridad;
import com.fleetguard360.alert_management.persistence.entity.TipoAlerta;
import com.fleetguard360.alert_management.persistence.repository.NivelPrioridadRepository;
import com.fleetguard360.alert_management.persistence.repository.TipoAlertaRepository;

import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaCreateRequest;
import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaResponse;
import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaUpdateRequest;
import com.fleetguard360.alert_management.service.exception.ConflictException;
import com.fleetguard360.alert_management.service.exception.NotFoundException;
import com.fleetguard360.alert_management.service.interfaces.TipoAlertaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TipoAlertaServiceImpl implements TipoAlertaService {

    private final TipoAlertaRepository tipoAlertaRepository;
    private final NivelPrioridadRepository nivelPrioridadRepository;
    private final TipoAlertaMapper mapper;

    @Override
    public TipoAlertaResponse create(TipoAlertaCreateRequest request) {
        log.debug("Creando TipoAlerta nombre='{}'", request.nombre());
        String normalizedNombre = request.nombre().trim();

        if (tipoAlertaRepository.existsByNombreIgnoreCase(normalizedNombre)) {
            log.warn("Intento de crear TipoAlerta duplicado nombre='{}'", normalizedNombre);
            throw new ConflictException("Ya existe un TipoAlerta con ese nombre");
        }

        // Validar que existe el nivel de prioridad
        NivelPrioridad nivelPrioridad = nivelPrioridadRepository.findById(request.nivelPrioridadId())
                .orElseThrow(() -> NotFoundException.forResource("NivelPrioridad", "id", request.nivelPrioridadId()));

        TipoAlerta entity = new TipoAlerta();
        entity.setNombre(normalizedNombre);
        entity.setDescripcion(request.descripcion());
        entity.setNivelPrioridad(nivelPrioridad);
        entity.setTipoEncargado(request.tipoEncargado());

        TipoAlerta saved = tipoAlertaRepository.save(entity);
        log.info("TipoAlerta creado id={} nombre='{}' tipoEncargado={}", saved.getId(), saved.getNombre(), saved.getTipoEncargado());
        return mapper.toResponse(saved);
    }

    @Override
    public TipoAlertaResponse update(Integer id, TipoAlertaUpdateRequest request) {
        log.debug("Actualizando TipoAlerta id={}", id);
        TipoAlerta entity = tipoAlertaRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forResource("TipoAlerta", "id", id));

        if (request.nombre() != null) {
            String normalizedNombre = request.nombre().trim();
            if (!normalizedNombre.equalsIgnoreCase(entity.getNombre()) &&
                    tipoAlertaRepository.existsByNombreIgnoreCase(normalizedNombre)) {
                log.warn("Conflicto al actualizar TipoAlerta id={} a nombre='{}' (duplicado)", id, normalizedNombre);
                throw new ConflictException("Ya existe un TipoAlerta con ese nombre");
            }
            entity.setNombre(normalizedNombre);
        }

        if (request.descripcion() != null) {
            entity.setDescripcion(request.descripcion());
        }

        if (request.nivelPrioridadId() != null) {
            NivelPrioridad nivelPrioridad = nivelPrioridadRepository.findById(request.nivelPrioridadId())
                    .orElseThrow(() -> NotFoundException.forResource("NivelPrioridad", "id", request.nivelPrioridadId()));
            entity.setNivelPrioridad(nivelPrioridad);
        }

        if (request.tipoEncargado() != null) {
            entity.setTipoEncargado(request.tipoEncargado());
        }

        TipoAlerta saved = tipoAlertaRepository.save(entity);
        log.info("TipoAlerta actualizado id={}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Eliminando TipoAlerta id={}", id);
        if (!tipoAlertaRepository.existsById(id)) {
            throw NotFoundException.forResource("TipoAlerta", "id", id);
        }
        try {
            tipoAlertaRepository.deleteById(id);
            log.info("TipoAlerta eliminado id={}", id);
        } catch (DataIntegrityViolationException ex) {
            log.warn("No se puede eliminar TipoAlerta id={} por integridad referencial", id);
            throw new ConflictException("No se puede eliminar el TipoAlerta porque está referenciado por otras entidades");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TipoAlertaResponse getById(Integer id) {
        log.debug("Buscando TipoAlerta id={}", id);
        TipoAlerta entity = tipoAlertaRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forResource("TipoAlerta", "id", id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoAlertaResponse> listAll() {
        log.debug("Listando todos los TipoAlerta");
        return mapper.toResponses(tipoAlertaRepository.findAll());
    }
}
