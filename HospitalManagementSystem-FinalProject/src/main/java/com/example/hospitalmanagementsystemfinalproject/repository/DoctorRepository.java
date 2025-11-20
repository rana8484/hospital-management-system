package com.example.hospitalmanagementsystemfinalproject.repository;

import com.example.hospitalmanagementsystemfinalproject.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Optional<Doctor> findByEmail(String email);

    @Query("SELECT d.doctorId FROM Doctor d ORDER BY d.doctorId DESC LIMIT 1")
    Optional<String> findLastDoctorId();

    Doctor findByDoctorId(String doctorId);

    Optional<Doctor> findByDoctorIdAndPassword(String doctorId, String password);
}
