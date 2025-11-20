package com.example.hospitalmanagementsystemfinalproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private int apptId;

    @Column(nullable = false)
    @NotNull(message = "Date is required")
    private LocalDate date;

    @Column(nullable = false)
    @NotNull(message = "Time is required")
    private LocalTime time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id")
    @NotNull(message = "Doctor must be specified")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    @NotNull(message = "Patient must be specified")
    private Patient patient;

    @Transient
    private String name;
    @Transient
    private String extraInfo;

    public Appointment() {}

    public Appointment(LocalDate date, LocalTime time, Doctor doctor, Patient patient) {
        this.date = date;
        this.time = time;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Appointment(Integer apptId, LocalDate date, LocalTime time, String name, String extraInfo) {
        this.apptId = apptId;
        this.date = date;
        this.time = time;
        this.name = name;
        this.extraInfo = extraInfo;
    }


    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
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
}
