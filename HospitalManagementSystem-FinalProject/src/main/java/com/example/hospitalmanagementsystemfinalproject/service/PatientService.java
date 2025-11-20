package com.example.hospitalmanagementsystemfinalproject.service;

import com.example.hospitalmanagementsystemfinalproject.exception.EmailAlreadyExistsException;
import com.example.hospitalmanagementsystemfinalproject.model.Patient;
import com.example.hospitalmanagementsystemfinalproject.repository.PatientRepository;
import com.example.hospitalmanagementsystemfinalproject.util.UserIdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserIdGenerator userIdGenerator;

    @Autowired
    public PatientService(PatientRepository patientRepository, UserIdGenerator userIdGenerator) {
        this.patientRepository = patientRepository;
        this.userIdGenerator = userIdGenerator;


    }

    public String addPatient(Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(patient.getEmail());
        }

        String patientId = userIdGenerator.generateUserId("patient");
        patient.setPatientId(patientId);

        Patient savedPatient = patientRepository.save(patient);
        return savedPatient != null ? patientId : null;
    }

    public List<Patient> getPatientsForDoctor(String doctorId) {
        return patientRepository.findPatientsByDoctorId(doctorId);
    }

    public Map<String, String> getPatientInfo(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Patient not found"));

        Map<String, String> info = new LinkedHashMap<>();
        info.put("username", patient.getName());
        info.put("email", patient.getEmail());
        info.put("phonenb", patient.getPhoneNumber());
        info.put("gender", patient.getGender());
        return info;
    }

    @Transactional
    public boolean updateProfile(String patientId, String newEmail, String newPhone, byte[] profilePhoto) {
        Optional<Patient> optional = patientRepository.findById(patientId);
        if (optional.isEmpty()) return false;

        Patient patient = optional.get();

        if (newEmail != null && !newEmail.isEmpty()) {
            patient.setEmail(newEmail);
        }

        if (newPhone != null && !newPhone.isEmpty()) {
            patient.setPhoneNumber(newPhone);
        }

        if (profilePhoto != null) {
            patient.setProfilePhoto(profilePhoto);
        }

        patientRepository.save(patient);
        return true;
    }

    @Transactional
    public boolean updatePassword(String patientId, String oldPassword, String newPassword) {
        Patient patient = patientRepository.findByPatientId(patientId);

        if (patient != null && patient.getPassword().equals(oldPassword)) {
            patient.setPassword(newPassword);
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    public byte[] getProfilePhoto(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId);
        return (patient != null) ? patient.getProfilePhoto() : null;
    }

    public boolean verifyLogin(String patientId, String password) {
        return patientRepository.findByPatientIdAndPassword(patientId, password).isPresent();
    }
}
