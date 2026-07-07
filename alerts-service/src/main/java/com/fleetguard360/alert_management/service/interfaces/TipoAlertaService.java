package com.fleetguard360.alert_management.service.interfaces;

import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaCreateRequest;
import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaResponse;
import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaUpdateRequest;

import java.util.List;

public interface TipoAlertaService {
    TipoAlertaResponse create(TipoAlertaCreateRequest request);
    TipoAlertaResponse update(Integer id, TipoAlertaUpdateRequest request);
    void delete(Integer id);
    TipoAlertaResponse getById(Integer id);
    List<TipoAlertaResponse> listAll();
}
