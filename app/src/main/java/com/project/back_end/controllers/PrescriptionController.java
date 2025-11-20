package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Services;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Services service;
    private final AppointmentService appointmentService;

    public PrescriptionController(PrescriptionService prescriptionService,
            Services service,
            AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }

    private String extractToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    // 3. Save Prescription
    @PostMapping("/save")
    public ResponseEntity<?> savePrescription(@RequestBody Prescription prescription,
            @RequestHeader("Authorization") String token) {
        token = extractToken(token);
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        // Update appointment status
        appointmentService.changeStatus(prescription.getAppointmentId(), Integer.parseInt("1")); // 1 can represent
                                                                                                 // 'Prescription added'

        String result = String.valueOf(prescriptionService.savePrescription(prescription));
        if ("success".equals(result)) {
            return ResponseEntity.ok("Prescription saved successfully");
        } else {
            return ResponseEntity.status(400).body(result);
        }
    }

    // 4. Get Prescription by Appointment ID
    @GetMapping("/get/{appointmentId}")
    public ResponseEntity<?> getPrescription(@PathVariable Long appointmentId,
            @RequestHeader("Authorization") String token) {
        token = extractToken(token);
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        ResponseEntity<Map<String, Object>> prescription = prescriptionService.getPrescription(appointmentId);
        if (prescription != null) {
            return ResponseEntity.ok(prescription);
        } else {
            return ResponseEntity.status(404).body("Prescription not found");
        }
    }
}
