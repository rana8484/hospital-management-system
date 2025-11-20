package com.example.hospitalmanagementsystemfinalproject.controller;

import com.example.hospitalmanagementsystemfinalproject.model.Patient;
import com.example.hospitalmanagementsystemfinalproject.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<String> addPatient(
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam String dob,
            @RequestParam String gender,
            @RequestParam String email,
            @RequestParam String phone) {

        Patient patient = new Patient();
        patient.setName(name);
        patient.setPassword(password);
        patient.setDateOfBirth(LocalDate.parse(dob));
        patient.setGender(gender);
        patient.setEmail(email);
        patient.setPhoneNumber(phone);

        String patientId = patientService.addPatient(patient);

        if (patientId != null) {
            return ResponseEntity.ok(patientId);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Patient>> getPatientsForDoctor(@PathVariable String doctorId) {
        List<Patient> patients = patientService.getPatientsForDoctor(doctorId);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<Map<String, String>> getPatientInfo(@PathVariable String id) {
        try {
            return ResponseEntity.ok(patientService.getPatientInfo(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/updateprofile")
    public ResponseEntity<String> updateProfile(
            @PathVariable("id") String patientId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) MultipartFile profilePhoto
    ) {
        try {
            byte[] imageBytes = (profilePhoto != null) ? profilePhoto.getBytes() : null;
            boolean updated = patientService.updateProfile(patientId, email, phone, imageBytes);
            if (updated) {
                return ResponseEntity.ok("Profile updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image.");
        }
    }

    @PutMapping("/{patientId}/updatepassword")
    public ResponseEntity<String> updatePassword(@PathVariable String patientId,
                                                 @RequestParam String oldPassword,
                                                 @RequestParam String newPassword) {
        boolean isUpdated = patientService.updatePassword(patientId, oldPassword, newPassword);
        if (isUpdated) {
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Incorrect old password");
        }
    }

    @GetMapping("/{patientId}/profilephoto")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable String patientId) {
        byte[] photo = patientService.getProfilePhoto(patientId);
        if (photo != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(photo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String patientId, @RequestParam String password) {
        boolean success = patientService.verifyLogin(patientId, password);
        if (success) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}

