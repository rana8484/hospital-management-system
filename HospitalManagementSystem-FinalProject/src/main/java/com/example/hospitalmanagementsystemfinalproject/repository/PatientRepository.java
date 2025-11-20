package com.example.hospitalmanagementsystemfinalproject.repository;

import com.example.hospitalmanagementsystemfinalproject.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    Optional<Patient> findByEmail(String email);

    @Query(value = "SELECT patient_id FROM patients ORDER BY patient_id DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLastPatientId();

    @Query("SELECT DISTINCT p FROM Appointment a JOIN a.patient p WHERE a.doctor.doctorId = :doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") String doctorId);

    Patient findByPatientId(String patientId);

    Optional<Patient> findByPatientIdAndPassword(String patientId, String password);
}
