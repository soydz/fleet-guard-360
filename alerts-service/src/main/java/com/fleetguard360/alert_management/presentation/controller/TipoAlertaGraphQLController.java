package com.fleetguard360.alert_management.presentation.controller;

import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaCreateRequest;
import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaResponse;
import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaUpdateRequest;
import com.fleetguard360.alert_management.service.interfaces.TipoAlertaService;
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
public class TipoAlertaGraphQLController {

    private final TipoAlertaService tipoAlertaService;

    @QueryMapping
    public TipoAlertaResponse tipoAlerta(@Argument Integer id) {
        log.debug("GraphQL Query: tipoAlerta id={}", id);
        return tipoAlertaService.getById(id);
    }

    @QueryMapping
    public List<TipoAlertaResponse> tipoAlertas() {
        log.debug("GraphQL Query: tipoAlertas");
        return tipoAlertaService.listAll();
    }

    @MutationMapping
    public TipoAlertaResponse createTipoAlerta(@Argument("input") @Valid TipoAlertaCreateRequest input) {
        log.debug("GraphQL Mutation: createTipoAlerta nombre='{}'", input.nombre());
        return tipoAlertaService.create(input);
    }

    @MutationMapping
    public TipoAlertaResponse updateTipoAlerta(@Argument Integer id, @Argument("input") @Valid TipoAlertaUpdateRequest input) {
        log.debug("GraphQL Mutation: updateTipoAlerta id={}", id);
        return tipoAlertaService.update(id, input);
    }

    @MutationMapping
    public Boolean deleteTipoAlerta(@Argument Integer id) {
        log.debug("GraphQL Mutation: deleteTipoAlerta id={}", id);
        tipoAlertaService.delete(id);
        return true;
    }
}
