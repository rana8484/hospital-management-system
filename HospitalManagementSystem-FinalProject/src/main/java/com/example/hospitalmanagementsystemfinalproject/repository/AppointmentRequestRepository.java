package com.example.hospitalmanagementsystemfinalproject.repository;

import com.example.hospitalmanagementsystemfinalproject.model.AppointmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Integer> {

    @Query(value = """
    SELECT 
        ar.request_id, 
        ar.requested_date, 
        ar.requested_time, 
        p.username
    FROM appointment_requests ar
    JOIN patients p ON ar.patient_id = p.patient_id
    WHERE ar.doctor_id = :doctorId 
      AND ar.status = 'PENDING'
      AND TIMESTAMP(ar.requested_date, ar.requested_time) >= NOW()
    """, nativeQuery = true)
    List<Object[]> findPendingRequestsByDoctorId(@Param("doctorId") String doctorId);

    @Query(value = """
        SELECT COUNT(*) FROM appointment_requests 
        WHERE requested_date = :date 
        AND requested_time = :time 
        AND (doctor_id = :doctorId OR patient_id = :patientId) 
        AND status IN ('PENDING', 'ACCEPTED')
        """, nativeQuery = true)
    int countConflicts(
            @Param("doctorId") String doctorId,
            @Param("patientId") String patientId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );

}
