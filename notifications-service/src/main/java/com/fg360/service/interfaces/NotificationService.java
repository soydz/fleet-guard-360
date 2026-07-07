package com.fg360.service.interfaces;

import com.fg360.presentation.controller.dto.AlertDTO;

public interface NotificationService {

    void handleEmail(AlertDTO alertDTO);

    void handlePush(AlertDTO alertDTO);
}
