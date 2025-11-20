package com.example.hospitalmanagementsystemfinalproject.service;

import com.example.hospitalmanagementsystemfinalproject.exception.EmailAlreadyExistsException;
import com.example.hospitalmanagementsystemfinalproject.model.Department;
import com.example.hospitalmanagementsystemfinalproject.model.Doctor;
import com.example.hospitalmanagementsystemfinalproject.repository.DepartmentRepository;
import com.example.hospitalmanagementsystemfinalproject.repository.DoctorRepository;
import com.example.hospitalmanagementsystemfinalproject.util.UserIdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final UserIdGenerator userIdGenerator;

    @Autowired
    public DoctorService(DoctorRepository doctorRepo, DepartmentRepository deptRepo, UserIdGenerator userIdGen) {
        this.doctorRepository = doctorRepo;
        this.departmentRepository = deptRepo;
        this.userIdGenerator = userIdGen;
    }

    public String addDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(doctor.getEmail());
        }

        String doctorId = userIdGenerator.generateUserId("doctor");
        doctor.setDoctorId(doctorId);

        Doctor savedDoctor = doctorRepository.save(doctor);
        return savedDoctor != null ? doctorId : null;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Map<String, String> getDoctorInfo(String id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Doctor not found"));

        Map<String, String> info = new LinkedHashMap<>();
        info.put("username", doctor.getName());
        info.put("email", doctor.getEmail());
        info.put("phonenb", doctor.getPhoneNumber());
        info.put("gender", doctor.getGender());

        Department dept = doctor.getDepartment();
        info.put("department_name", dept != null ? dept.getDepartmentName() : "N/A");
        return info;
    }

    @Transactional
    public boolean updateProfile(String doctorId, String newEmail, String newPhone, byte[] profilePhoto) {
        Optional<Doctor> optional = doctorRepository.findById(doctorId);
        if (optional.isEmpty()) return false;

        Doctor doctor = optional.get();

        if (newEmail != null && !newEmail.isEmpty()) {
            doctor.setEmail(newEmail);
        }

        if (newPhone != null && !newPhone.isEmpty()) {
            doctor.setPhoneNumber(newPhone);
        }

        if (profilePhoto != null) {
            doctor.setProfilePhoto(profilePhoto);
        }

        doctorRepository.save(doctor);
        return true;
    }


    @Transactional
    public boolean updatePassword(String doctorId, String oldPassword, String newPassword) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId);

        if (doctor != null && doctor.getPassword().equals(oldPassword)) {
            doctor.setPassword(newPassword);
            doctorRepository.save(doctor);
            return true;
        }
        return false;
    }

    public byte[] getProfilePhoto(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId);
        return (doctor != null) ? doctor.getProfilePhoto() : null;
    }

    public boolean verifyLogin(String doctorId, String password) {
        return doctorRepository.findByDoctorIdAndPassword(doctorId, password).isPresent();
    }
}

