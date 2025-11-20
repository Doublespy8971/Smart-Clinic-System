package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Patient patient;

    @Future
    private LocalDateTime date;

    @NotNull
    private int status; // 0 = scheduled, 1 = completed

    @Transient
    public LocalDateTime getEndTime() {
        if (date == null) return null;
        return date.plusHours(1);
    }

    @Transient
    public LocalDate getAppointmentDate() {
        if (date == null) return null;
        return date.toLocalDate();
    }

    @Transient
    public LocalTime getAppointmentTimeOnly() {
        if (date == null) return null;
        return date.toLocalTime();
    }
}
