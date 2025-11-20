package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public List<String> validateAppointment(Long doctorId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) return Collections.emptyList();

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDateBetween(
                doctorId, date.atStartOfDay(), date.atTime(23, 59));

        Set<String> bookedTimes = appointments.stream()
                .map(a -> a.getDate().toLocalTime().toString())
                .collect(Collectors.toSet());

        List<String> available = doctor.getAvailableTimes().stream()
                .filter(time -> !bookedTimes.contains(time))
                .collect(Collectors.toList());

        return available;
    }

    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) return -1;
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        try {
            if (!doctorRepository.existsById(doctor.getId())) return -1;
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int deleteDoctor(Long doctorId) {
        try {
            if (!doctorRepository.existsById(doctorId)) return -1;
            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public String validateDoctor(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor == null) return "Doctor not found";
        if (!doctor.getPassword().equals(password)) return "Invalid password";
        return tokenService.generateToken(doctor.getId(), "doctor");
    }

    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameLike(name);
    }

    public List<Doctor> filterDoctorsByNameSpecilityandTime(String name, String specialty, String timePeriod) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterDoctorByTimeList(doctors, timePeriod);
    }

    public List<Doctor> filterDoctorByTime(String timePeriod) {
        List<Doctor> doctors = doctorRepository.findAll();
        return filterDoctorByTimeList(doctors, timePeriod);
    }

    public List<Doctor> filterDoctorByNameAndTime(String name, String timePeriod) {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        return filterDoctorByTimeList(doctors, timePeriod);
    }

    public List<Doctor> filterDoctorByNameAndSpecility(String name, String specialty) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    public List<Doctor> filterDoctorByTimeAndSpecility(String timePeriod, String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterDoctorByTimeList(doctors, timePeriod);
    }

    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    public List<Doctor> filterDoctorsByTime(String timePeriod) {
        List<Doctor> doctors = doctorRepository.findAll();
        return filterDoctorByTimeList(doctors, timePeriod);
    }

    // ----------------------
    // Private helper methods
    // ----------------------
    private List<Doctor> filterDoctorByTimeList(List<Doctor> doctors, String timePeriod) {
        if (timePeriod == null) return doctors;

        boolean isAM = timePeriod.equalsIgnoreCase("AM");
        boolean isPM = timePeriod.equalsIgnoreCase("PM");

        return doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes().stream().anyMatch(time -> {
                    LocalTime t = LocalTime.parse(time);
                    return (isAM && t.getHour() < 12) || (isPM && t.getHour() >= 12);
                }))
                .collect(Collectors.toList());
    }
}
