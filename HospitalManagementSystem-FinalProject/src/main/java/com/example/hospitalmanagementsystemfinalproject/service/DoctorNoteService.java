package com.example.hospitalmanagementsystemfinalproject.service;

import com.example.hospitalmanagementsystemfinalproject.model.Doctor;
import com.example.hospitalmanagementsystemfinalproject.model.DoctorNote;
import com.example.hospitalmanagementsystemfinalproject.model.Patient;
import com.example.hospitalmanagementsystemfinalproject.repository.DoctorNoteRepository;
import com.example.hospitalmanagementsystemfinalproject.repository.DoctorRepository;
import com.example.hospitalmanagementsystemfinalproject.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoctorNoteService {

    @Autowired
    private DoctorNoteRepository doctorNoteRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public void saveDoctorNote(String doctorId, String patientId, String note) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        DoctorNote doctorNote = new DoctorNote(doctor, patient, note);
        doctorNoteRepository.save(doctorNote);
    }

    public List<Map<String, String>> getPatientNotes(String doctorId, String patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));

        List<DoctorNote> notes = doctorNoteRepository
                .findByDoctorAndPatientOrderByCreatedAtDesc(doctor, patient);

        List<Map<String, String>> response = new ArrayList<>();
        for (DoctorNote note : notes) {
            Map<String, String> map = new HashMap<>();
            map.put("createdAt", note.getCreatedAt().toString());
            map.put("note", note.getNote());
            response.add(map);
        }
        return response;
    }
}

