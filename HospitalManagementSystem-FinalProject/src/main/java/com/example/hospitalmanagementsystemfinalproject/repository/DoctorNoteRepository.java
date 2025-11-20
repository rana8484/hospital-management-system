package com.example.hospitalmanagementsystemfinalproject.repository;

import com.example.hospitalmanagementsystemfinalproject.model.Doctor;
import com.example.hospitalmanagementsystemfinalproject.model.DoctorNote;
import com.example.hospitalmanagementsystemfinalproject.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorNoteRepository extends JpaRepository<DoctorNote, Integer> {
    List<DoctorNote> findByDoctorAndPatientOrderByCreatedAtDesc(Doctor doctor, Patient patient);
}

