package com.example.hospitalmanagementsystemfinalproject.controller;

import com.example.hospitalmanagementsystemfinalproject.exception.EmailAlreadyExistsException;
import com.example.hospitalmanagementsystemfinalproject.model.Department;
import com.example.hospitalmanagementsystemfinalproject.model.Doctor;
import com.example.hospitalmanagementsystemfinalproject.repository.DepartmentRepository;
import com.example.hospitalmanagementsystemfinalproject.service.DoctorService;
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
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addDoctor(
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam String dob,
            @RequestParam String gender,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String departmentName) {

        Department dept = departmentRepository.findByDepartmentName(departmentName);
        if (dept == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid department name");
        }

        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setPassword(password);
        doctor.setDateOfBirth(LocalDate.parse(dob));
        doctor.setGender(gender);
        doctor.setEmail(email);
        doctor.setPhoneNumber(phone);
        doctor.setDepartment(dept);

        try {
            String doctorId = doctorService.addDoctor(doctor);
            if (doctorId != null) {
                return ResponseEntity.ok(doctorId);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Doctor could not be added.");
            }
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<Map<String, String>> getDoctorInfo(@PathVariable String id) {
        try {
            return ResponseEntity.ok(doctorService.getDoctorInfo(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/updateprofile")
    public ResponseEntity<String> updateProfile(
            @PathVariable("id") String doctorId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) MultipartFile profilePhoto
    ) {
        try {
            byte[] imageBytes = (profilePhoto != null) ? profilePhoto.getBytes() : null;
            boolean updated = doctorService.updateProfile(doctorId, email, phone, imageBytes);
            if (updated) {
                return ResponseEntity.ok("Profile updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image.");
        }
    }

    @PutMapping("/{doctorId}/updatepassword")
    public ResponseEntity<String> updatePassword(@PathVariable String doctorId,
                                                 @RequestParam String oldPassword,
                                                 @RequestParam String newPassword) {
        boolean isUpdated = doctorService.updatePassword(doctorId, oldPassword, newPassword);
        if (isUpdated) {
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Incorrect old password");
        }
    }

    @GetMapping("/{doctorId}/profilephoto")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable String doctorId) {
        byte[] photo = doctorService.getProfilePhoto(doctorId);
        if (photo != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(photo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String doctorId, @RequestParam String password) {
        boolean success = doctorService.verifyLogin(doctorId, password);
        if (success) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}

