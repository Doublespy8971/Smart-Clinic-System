package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Services;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final Services service;

    public PatientController(PatientService patientService, Services service) {
        this.patientService = patientService;
        this.service = service;
    }

    // 3. Get Patient Details
    @GetMapping("/get/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        Patient patient = service.getPatientFromToken(token); // Implement in Services
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }

        return ResponseEntity.ok(patient);
    }

    // 4. Create Patient
    @PostMapping("/create")
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        if (!service.validatePatient(patient.getEmail(), patient.getPhone())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient already exists");
        }

        int result = patientService.createPatient(patient);
        if (result == 1) {
            return ResponseEntity.ok("Patient created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create patient");
        }
    }

    // 5. Patient Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        return service.validatePatientLogin(login.getEmail(), login.getPassword());
    }

    // 6. Get Patient Appointments
    @GetMapping("/appointments/{patientId}/{token}/{user}")
    public ResponseEntity<?> getPatientAppointment(@PathVariable Long patientId,
                                                   @PathVariable String token,
                                                   @PathVariable String user) {
        if (!service.validateToken(token, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        return ResponseEntity.ok(patientService.getPatientAppointment(patientId));
    }

    // 7. Filter Patient Appointments
    @GetMapping("/appointments/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointment(@PathVariable String condition,
                                                      @PathVariable String name,
                                                      @PathVariable String token) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        return service.filterPatient(token, condition, name);
    }
}
