package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public List<AppointmentDTO> getPatientAppointment(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AppointmentDTO> filterByCondition(Long patientId, String condition) {
        int status;
        if ("future".equalsIgnoreCase(condition)) {
            status = 0;
        } else if ("past".equalsIgnoreCase(condition)) {
            status = 1;
        } else {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }

        List<Appointment> appointments = appointmentRepository.findByPatient_IdAndStatusOrderByDateAsc(patientId, status);
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AppointmentDTO> filterByDoctor(Long patientId, String doctorName) {
        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientId(doctorName, patientId);
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AppointmentDTO> filterByDoctorAndCondition(Long patientId, String doctorName, String condition) {
        int status;
        if ("future".equalsIgnoreCase(condition)) {
            status = 0;
        } else if ("past".equalsIgnoreCase(condition)) {
            status = 1;
        } else {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }

        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status);
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Patient getPatientDetails(String token) {
        String email = tokenService.extractEmail(token);
        return patientRepository.findByEmail(email);
    }

    // Helper method to convert Appointment to AppointmentDTO using your custom 'of' method
    private AppointmentDTO convertToDTO(Appointment appointment) {
        return AppointmentDTO.of(
                appointment.getId(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getName(),
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                appointment.getPatient().getEmail(),
                appointment.getPatient().getPhone(),
                appointment.getPatient().getAddress(),
                appointment.getDate(),
                appointment.getStatus()
        );
    }
}
