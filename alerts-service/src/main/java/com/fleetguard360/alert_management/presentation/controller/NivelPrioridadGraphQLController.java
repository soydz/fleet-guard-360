package com.fleetguard360.alert_management.presentation.controller;

import com.fleetguard360.alert_management.presentation.DTO.nivelprioridad.NivelPrioridadCreateRequest;
import com.fleetguard360.alert_management.presentation.DTO.nivelprioridad.NivelPrioridadResponse;
import com.fleetguard360.alert_management.presentation.DTO.nivelprioridad.NivelPrioridadUpdateRequest;
import com.fleetguard360.alert_management.service.interfaces.NivelPrioridadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NivelPrioridadGraphQLController {

    private final NivelPrioridadService nivelPrioridadService;

    @QueryMapping
    public NivelPrioridadResponse nivelPrioridad(@Argument Integer id) {
        log.debug("GraphQL Query: nivelPrioridad id={}", id);
        return nivelPrioridadService.getById(id);
    }

    @QueryMapping
    public List<NivelPrioridadResponse> nivelesPrioridad() {
        log.debug("GraphQL Query: nivelesPrioridad");
        return nivelPrioridadService.listAll();
    }

    @MutationMapping
    public NivelPrioridadResponse createNivelPrioridad(@Argument("input") @Valid NivelPrioridadCreateRequest input) {
        log.debug("GraphQL Mutation: createNivelPrioridad nombre='{}'", input.nombre());
        return nivelPrioridadService.create(input);
    }

    @MutationMapping
    public NivelPrioridadResponse updateNivelPrioridad(@Argument Integer id, @Argument("input") @Valid NivelPrioridadUpdateRequest input) {
        log.debug("GraphQL Mutation: updateNivelPrioridad id={}", id);
        return nivelPrioridadService.update(id, input);
    }

    @MutationMapping
    public Boolean deleteNivelPrioridad(@Argument Integer id) {
        log.debug("GraphQL Mutation: deleteNivelPrioridad id={}", id);
        nivelPrioridadService.delete(id);
        return true;
    }
}
