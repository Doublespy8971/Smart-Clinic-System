package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Services;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Services service;

    public AppointmentController(AppointmentService appointmentService, Services service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // 3. Get Appointments
    @GetMapping("/get/{token}")
    public ResponseEntity<?> getAppointments(@RequestParam String date,
                                             @RequestParam(required = false) String patientName,
                                             @PathVariable String token) {
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        LocalDateTime appointmentDate = LocalDateTime.parse(date);
        List<Appointment> appointments = appointmentService.getAppointments(null, appointmentDate, patientName);
        return ResponseEntity.ok(appointments);
    }

    // 4. Book Appointment
    @PostMapping("/book/{token}")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment,
                                             @PathVariable String token) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        int result = appointmentService.bookAppointment(appointment);
        if (result == 1) {
            return ResponseEntity.ok("Appointment booked successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to book appointment");
        }
    }

    // 5. Update Appointment
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateAppointment(@RequestBody Appointment appointment,
                                               @PathVariable String token) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        String result = appointmentService.updateAppointment(appointment.getId(), appointment, token);
        if (result.equals("Appointment updated successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    // 6. Cancel Appointment
    @DeleteMapping("/cancel/{appointmentId}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId,
                                               @PathVariable String token) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        String result = appointmentService.cancelAppointment(appointmentId, token);
        if (result.equals("Appointment cancelled successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
