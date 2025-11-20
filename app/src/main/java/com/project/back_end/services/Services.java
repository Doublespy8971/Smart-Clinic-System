package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Services {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Services(TokenService tokenService,
                    AdminRepository adminRepository,
                    DoctorRepository doctorRepository,
                    PatientRepository patientRepository,
                    DoctorService doctorService,
                    PatientService patientService) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // 3. Validate Token
    public boolean validateToken(String token, String role) {
        return tokenService.validateToken(token, role);
    }

    // 4. Validate Admin Login
    public ResponseEntity<?> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);

            if (admin == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not found");
            }

            if (!admin.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }

            // generate token using admin ID & username
            String token = tokenService.generateToken(admin.getId(), admin.getUsername());
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    // 5. Filter Doctors
    public List<Doctor> filterDoctor(String name, String specialty, String timePeriod) {
        return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, timePeriod);
    }

    // 6. Validate Appointment
    public List<String> validateAppointment(Long doctorId, String dateTime) {
        return doctorService.validateAppointment(doctorId, dateTime);
    }

    // 7. Validate Patient Registration
    public boolean validatePatient(String email, String phone) {
        Patient patient = patientRepository.findByEmailOrPhone(email, phone);
        return patient == null; // true means validation pass (unique)
    }

    // 8. Validate Patient Login
    public ResponseEntity<?> validatePatientLogin(String email, String password) {
        try {
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Patient not found");
            }

            if (!patient.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }

            // generate token using patient ID and email
            String token = tokenService.generateToken(patient.getId(), patient.getEmail());
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    public Patient getPatientFromToken(String token) {
        try {
            String email = tokenService.extractEmail(token);
            return patientRepository.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // 9. Filter Patient Appointments
    public ResponseEntity<?> filterPatient(String token, String condition, String doctorName) {
        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Patient not found");
            }

            Long patientId = patient.getId();

            if (condition != null && doctorName != null) {
                return ResponseEntity.ok(
                        patientService.filterByDoctorAndCondition(patientId, doctorName, condition)
                );
            }

            if (condition != null) {
                return ResponseEntity.ok(patientService.filterByCondition(patientId, condition));
            }

            if (doctorName != null) {
                return ResponseEntity.ok(patientService.filterByDoctor(patientId, doctorName));
            }

            return ResponseEntity.ok(patientService.getPatientAppointment(patientId));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching appointments");
        }
    }
}
