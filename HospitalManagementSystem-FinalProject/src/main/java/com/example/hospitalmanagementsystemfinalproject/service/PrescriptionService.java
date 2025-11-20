package com.example.hospitalmanagementsystemfinalproject.service;

import com.example.hospitalmanagementsystemfinalproject.model.Doctor;
import com.example.hospitalmanagementsystemfinalproject.model.Patient;
import com.example.hospitalmanagementsystemfinalproject.model.Prescription;
import com.example.hospitalmanagementsystemfinalproject.model.PrescriptionMedication;
import com.example.hospitalmanagementsystemfinalproject.repository.DoctorRepository;
import com.example.hospitalmanagementsystemfinalproject.repository.PatientRepository;
import com.example.hospitalmanagementsystemfinalproject.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public void addPrescription(String doctorId, String patientId, List<String> medications) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));

        Prescription prescription = new Prescription();
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setPrescriptionDate(LocalDate.now());

        List<PrescriptionMedication> medicationList = new ArrayList<>();
        for (String medName : medications) {
            PrescriptionMedication med = new PrescriptionMedication();
            med.setMedicationName(medName.trim());
            med.setPrescription(prescription);
            medicationList.add(med);
        }

        prescription.setMedications(medicationList);
        prescriptionRepository.save(prescription);
    }

    public Map<String, List<String>> getPatientPrescriptions(String doctorId, String patientId) {
        List<Object[]> results = prescriptionRepository.findPrescriptionsWithMedications(doctorId, patientId);
        Map<String, List<String>> prescriptionsMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            String date = row[0].toString();
            String medication = (row[1] != null) ? row[1].toString() : null;

            prescriptionsMap.putIfAbsent(date, new ArrayList<>());
            if (medication != null) {
                prescriptionsMap.get(date).add(medication);
            }
        }

        return prescriptionsMap;
    }

    public Map<String, List<String>> getAllPrescriptions(String patientId) {
        List<Object[]> results = prescriptionRepository.findAllPrescriptionsByPatientId(patientId);
        Map<String, List<String>> prescriptions = new LinkedHashMap<>();

        for (Object[] row : results) {
            String date = row[0].toString();
            String medication = row[1].toString();

            prescriptions.computeIfAbsent(date, k -> new ArrayList<>()).add(medication);
        }

        return prescriptions;
    }

}

