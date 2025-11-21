package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Services;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Services service;

    public DoctorController(DoctorService doctorService, Services service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    private String extractToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    // 3. Get Doctor Availability
    @GetMapping("/availability/{user}/{doctorId}/{date}")
    public ResponseEntity<?> getDoctorAvailability(@PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @RequestHeader("Authorization") String token) {
        token = extractToken(token);
        if (!service.validateToken(token, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        List<String> availability = doctorService.validateAppointment(doctorId, date);
        return ResponseEntity.ok(availability);
    }

    // 4. Get All Doctors
    @GetMapping("/all")
    public ResponseEntity<?> getDoctor() {
        List<Doctor> doctors = doctorService.getDoctors();
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    // 5. Save Doctor
    @PostMapping("/save")
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor, @RequestHeader("Authorization") String token) {
        token = extractToken(token);
        if (!service.validateToken(token, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        int result = doctorService.saveDoctor(doctor);
        if (result == -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor already exists");
        } else if (result == 1) {
            return ResponseEntity.ok("Doctor added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add doctor");
        }
    }

    // 6. Doctor Login
    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(@RequestBody Login login) {
        String response = doctorService.validateDoctor(login.getEmail(), login.getPassword());
        if ("Doctor not found".equals(response) || "Invalid password".equals(response)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Collections.singletonMap("message", response));
        }
        return ResponseEntity.ok(java.util.Collections.singletonMap("token", response));
    }

    // 7. Update Doctor
    @PutMapping("/update")
    public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor, @RequestHeader("Authorization") String token) {
        token = extractToken(token);
        if (!service.validateToken(token, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        int result = doctorService.updateDoctor(doctor);
        if (result == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        } else if (result == 1) {
            return ResponseEntity.ok("Doctor updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update doctor");
        }
    }

    // 8. Delete Doctor
    @DeleteMapping("/delete/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long doctorId, @RequestHeader("Authorization") String token) {
        token = extractToken(token);
        if (!service.validateToken(token, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        int result = doctorService.deleteDoctor(doctorId);
        if (result == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        } else if (result == 1) {
            return ResponseEntity.ok("Doctor deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete doctor");
        }
    }

    // 9. Filter Doctors
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<?> filter(@PathVariable(required = false) String name,
            @PathVariable(required = false) String time,
            @PathVariable(required = false) String speciality) {
        List<Doctor> doctors = service.filterDoctor(name, speciality, time);
        return ResponseEntity.ok(doctors);
    }
}
