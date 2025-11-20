package com.example.hospitalmanagementsystemfrontendfinal.model;

public class Doctor {
    private byte[] imageBytes;
    private String name;
    private String phoneNumber;
    private String email;
    private String department;
    private String doctorId;
    private String gender;

    public Doctor(byte[] imageBytes, String name, String phoneNumber, String email, String department, String doctorId, String gender) {
        this.imageBytes = imageBytes;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.department = department;
        this.doctorId = doctorId;
        this.gender = gender;
    }

    public byte[] getImageBytes() { return imageBytes; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public String getDoctorId() { return doctorId; }
    public String getGender() { return gender; }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}

