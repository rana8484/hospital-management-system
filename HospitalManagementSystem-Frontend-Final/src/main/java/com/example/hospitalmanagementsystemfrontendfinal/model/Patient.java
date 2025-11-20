package com.example.hospitalmanagementsystemfrontendfinal.model;

import java.time.LocalDate;

public class Patient {
    private byte[] imageBytes;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDate dateOfBirth;
    private String patientId;
    private String gender;

    public Patient() {}

    public Patient(byte[] imageBytes, String name, String phoneNumber, String email, LocalDate dateOfBirth, String patientId, String gender) {
        this.imageBytes = imageBytes;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.patientId = patientId;
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getImageBytes() { return imageBytes; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getPatientId() { return patientId; }
    public String getGender() { return gender; }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}

