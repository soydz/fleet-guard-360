package com.fleetguard360.panel_service.presentation.controller;

import com.fleetguard360.panel_service.service.interfaces.AlertService;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class ExportCSVController {

    private final AlertService alertService;
    private final AlertController alertController;


    public ExportCSVController(AlertService alertService, AlertController alertController) {
        this.alertService = alertService;
        this.alertController = alertController;
    }

    @GetMapping("/export/alerts.csv")
    public void exportAlertsCsv(@RequestParam String token, HttpServletResponse response) throws IOException {

        if (!alertController.isValidToken(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired download token");
        }

        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=alerts.csv");

        try (CSVWriter writer = new CSVWriter(response.getWriter())) {
            // Encabezado
            String[] header = {"id", "alertType", "driver", "generatingUnit", "generationDate", "priority", "responsible", "state"};
            writer.writeNext(header);

            // Datos
            alertService.getAllAlerts().forEach( alert -> writer.writeNext(
                    new String[] {
                            String.valueOf(alert.id()),
                            String.valueOf(alert.alertType()),
                            String.valueOf(alert.driver()),
                            String.valueOf(alert.generatingUnit()),
                            String.valueOf(alert.generationDate()),
                            String.valueOf(alert.priority()),
                            String.valueOf(alert.responsible()),
                            String.valueOf(alert.state())
                    }
            ));
        }
    }
}
