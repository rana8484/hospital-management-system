package com.example.hospitalmanagementsystemfinalproject.util;

import com.example.hospitalmanagementsystemfinalproject.repository.DoctorRepository;
import com.example.hospitalmanagementsystemfinalproject.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserIdGenerator {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public UserIdGenerator(DoctorRepository doctorRepo, PatientRepository patientRepo) {
        this.doctorRepository = doctorRepo;
        this.patientRepository = patientRepo;
    }

    public String generateUserId(String userType) {
        String prefix = userType.equalsIgnoreCase("doctor") ? "D" : "P";
        int base = 10000;
        int max = 99999;

        String lastId = userType.equalsIgnoreCase("doctor")
                ? doctorRepository.findLastDoctorId().orElse(null)
                : patientRepository.findLastPatientId().orElse(null);

        int lastNum = 0;
        if (lastId != null && lastId.length() > 1) {
            lastNum = Integer.parseInt(lastId.substring(1));
        }

        int newNum = (lastNum == 0) ? base : lastNum + 1;

        if (newNum > max) throw new IllegalStateException("User ID limit exceeded!");

        return prefix + newNum;
    }
}

