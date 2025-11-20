package com.example.hospitalmanagementsystemfinalproject.controller;

import com.example.hospitalmanagementsystemfinalproject.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/add")
    public ResponseEntity<String> addPrescription(
            @RequestParam String doctorId,
            @RequestParam String patientId,
            @RequestParam List<String> medications) {

        prescriptionService.addPrescription(doctorId, patientId, medications);
        return ResponseEntity.ok("Prescription added successfully.");
    }

    @GetMapping("/getbydoctorpatient")
    public ResponseEntity<Map<String, List<String>>> getPrescriptions(
            @RequestParam String doctorId,
            @RequestParam String patientId) {

        Map<String, List<String>> prescriptions = prescriptionService.getPatientPrescriptions(doctorId, patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/getbypatient")
    public ResponseEntity<Map<String, List<String>>> getAllPrescriptions(
            @RequestParam String patientId) {

        Map<String, List<String>> prescriptions = prescriptionService.getAllPrescriptions(patientId);
        return ResponseEntity.ok(prescriptions);
    }

}

