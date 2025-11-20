package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              TokenService tokenService,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // 4. Book Appointment
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // 5. Update Appointment
    @Transactional
    public String updateAppointment(Long appointmentId, Appointment updated, String token) {

        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);
        if (patient == null) return "Unauthorized";

        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment existing = optional.get();

        if (!existing.getPatient().getId().equals(patient.getId()))
            return "Unauthorized";

        // Check doctor availability
        LocalDateTime start = updated.getDate();
        LocalDateTime end = start.plusHours(1);

        List<Appointment> conflicts = appointmentRepository
                .findByDoctorIdAndDateBetween(existing.getDoctor().getId(), start, end);

        // remove the same appointment from conflict check
        conflicts.removeIf(a -> a.getId() == existing.getId());


        if (!conflicts.isEmpty())
            return "Doctor not available at this time";

        existing.setDate(updated.getDate());
        existing.setStatus(updated.getStatus());
        appointmentRepository.save(existing);

        return "Appointment updated successfully";
    }

    // 6. Cancel Appointment
    @Transactional
    public String cancelAppointment(Long appointmentId, String token) {

        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);
        if (patient == null) return "Unauthorized";

        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment existing = optional.get();

        if (!existing.getPatient().getId().equals(patient.getId()))
            return "Unauthorized";

        appointmentRepository.delete(existing);
        return "Appointment cancelled successfully";
    }

    // 7. Get Appointments for Doctor + Date + Optional Patient Name
    @Transactional
    public List<Appointment> getAppointments(Long doctorId,
                                             LocalDateTime date,
                                             String patientName) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        if (patientName == null || patientName.isEmpty()) {
            return appointmentRepository.findByDoctorIdAndDateBetween(doctorId, startOfDay, endOfDay);
        }
        return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndDateBetween(
                doctorId, patientName, startOfDay, endOfDay
        );
    }

    // 8. Change Appointment Status
    @Transactional
    public String changeStatus(Long appointmentId, int status) {

        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment appointment = optional.get();
        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        return "Status updated";
    }
}
