package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 1. Appointments for a doctor within a time range
    List<Appointment> findByDoctorIdAndDateBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    // 2. Appointments for a doctor with patient name (case-insensitive) in a time range
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndDateBetween(
            Long doctorId, String patientName, LocalDateTime start, LocalDateTime end
    );

    // 3. Delete all appointments by doctor ID
    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    // 4. Find all appointments for a specific patient
    List<Appointment> findByPatientId(Long patientId);

    // 5. Find appointments for a patient with specific status, ordered by time
    List<Appointment> findByPatient_IdAndStatusOrderByDateAsc(Long patientId, int status);

    // 6. Filter appointments by doctor name (LIKE) and patient ID
    @Query("SELECT a FROM Appointment a WHERE a.doctor.name LIKE %:doctorName% AND a.patient.id = :patientId")
    List<Appointment> filterByDoctorNameAndPatientId(@Param("doctorName") String doctorName,
                                                     @Param("patientId") Long patientId);

    // 7. Filter appointments by doctor name (LIKE), patient ID, and status
    @Query("SELECT a FROM Appointment a WHERE a.doctor.name LIKE %:doctorName% AND a.patient.id = :patientId AND a.status = :status")
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(@Param("doctorName") String doctorName,
                                                              @Param("patientId") Long patientId,
                                                              @Param("status") int status);

    // 8. Update status of an appointment by ID
    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
    void updateStatus(@Param("status") int status, @Param("id") long id);
}
