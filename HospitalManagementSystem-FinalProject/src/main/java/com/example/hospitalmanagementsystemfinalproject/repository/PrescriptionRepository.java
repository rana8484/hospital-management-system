package com.example.hospitalmanagementsystemfinalproject.repository;

import com.example.hospitalmanagementsystemfinalproject.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    @Query(value = """
        SELECT p.prescription_date, pm.medication_name
        FROM prescriptions p
        LEFT JOIN prescription_medications pm ON p.prescription_id = pm.prescription_id
        WHERE p.doctor_id = :doctorId AND p.patient_id = :patientId
        ORDER BY p.prescription_date DESC
        """, nativeQuery = true)
    List<Object[]> findPrescriptionsWithMedications(@Param("doctorId") String doctorId,
                                                    @Param("patientId") String patientId);

    @Query(value = """
    SELECT p.prescription_date, pm.medication_name
    FROM prescriptions p
    JOIN prescription_medications pm ON p.prescription_id = pm.prescription_id
    WHERE p.patient_id = :patientId
    ORDER BY p.prescription_date DESC
    """, nativeQuery = true)
    List<Object[]> findAllPrescriptionsByPatientId(@Param("patientId") String patientId);

}

