package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
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

    public Appointment() {
    }

    public Appointment(Doctor doctor, Patient patient, LocalDateTime date, int status) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Transient
    public LocalDateTime getEndTime() {
        if (date == null)
            return null;
        return date.plusHours(1);
    }

    @Transient
    public LocalDate getAppointmentDate() {
        if (date == null)
            return null;
        return date.toLocalDate();
    }

    @Transient
    public LocalTime getAppointmentTimeOnly() {
        if (date == null)
            return null;
        return date.toLocalTime();
    }
}
