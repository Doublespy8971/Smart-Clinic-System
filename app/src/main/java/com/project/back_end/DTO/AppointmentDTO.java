package com.project.back_end.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;

    private Long doctorId;
    private String doctorName;

    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;

    private LocalDateTime appointmentTime;
    private int status;

    // Derived fields
    private LocalDate appointmentDate;
    private LocalTime appointmentTimeOnly;
    private LocalDateTime endTime;

    // Custom builder method to calculate derived fields
    public static AppointmentDTO of(Long id, Long doctorId, String doctorName,
                                    Long patientId, String patientName, String patientEmail,
                                    String patientPhone, String patientAddress,
                                    LocalDateTime appointmentTime, int status) {
        AppointmentDTO dto = new AppointmentDTO(
                id, doctorId, doctorName,
                patientId, patientName, patientEmail,
                patientPhone, patientAddress,
                appointmentTime, status,
                null, null, null
        );
        if (appointmentTime != null) {
            dto.setAppointmentDate(appointmentTime.toLocalDate());
            dto.setAppointmentTimeOnly(appointmentTime.toLocalTime());
            dto.setEndTime(appointmentTime.plusHours(1));
        }
        return dto;
    }
}
