package com.example.hospitalmanagementsystemfinalproject.repository;

import com.example.hospitalmanagementsystemfinalproject.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {


    @Query(value = """
    SELECT * FROM appointments 
    WHERE patient_id = :patientId 
      AND (date > CURDATE() OR (date = CURDATE() AND time > CURTIME()))
    ORDER BY date ASC, time ASC
""", nativeQuery = true)
    List<Appointment> findUpcomingByPatientId(@Param("patientId") String patientId);

    @Query(value = """
    SELECT * FROM appointments 
    WHERE doctor_id = :doctorId 
      AND (date > CURDATE() OR (date = CURDATE() AND time > CURTIME()))
    ORDER BY date ASC, time ASC
""", nativeQuery = true)
    List<Appointment> findUpcomingByDoctorId(@Param("doctorId") String doctorId);

    @Query(value = """
    SELECT a.appointment_id, a.date, a.time, 
           CASE 
             WHEN :isDoctor = true THEN p.username 
             ELSE d.username 
           END AS name,
           CASE 
             WHEN :isDoctor = true THEN TIMESTAMPDIFF(YEAR, p.dateofbirth, CURDATE())
             ELSE dn.department_name 
           END AS extra_info
    FROM appointments a
    LEFT JOIN doctors d ON a.doctor_id = d.doctor_id
    LEFT JOIN departments dn ON d.department_id = dn.department_id
    LEFT JOIN patients p ON a.patient_id = p.patient_id
    WHERE (a.doctor_id = :userId OR a.patient_id = :userId)
      AND a.date = CURDATE()
      AND a.time > CURTIME()
    ORDER BY a.time ASC
    """, nativeQuery = true)
    List<Object[]> findDailyAppointments(@Param("userId") String userId, @Param("isDoctor") boolean isDoctor);

    @Query(value = "SELECT a.appointment_id, a.date, a.time, d.username AS doctor_name, dept.department_name " +
            "FROM appointments a " +
            "JOIN doctors d ON a.doctor_id = d.doctor_id " +
            "JOIN departments dept ON d.department_id = dept.department_id " +
            "WHERE a.patient_id = :patientId AND a.date < CURDATE() " +
            "ORDER BY a.date DESC, a.time DESC", nativeQuery = true)
    List<Object[]> findPreviousAppointmentsByPatientId(@Param("patientId") String patientId);


    @Query(value = "SELECT password FROM doctors WHERE doctor_id = :doctorId", nativeQuery = true)
    String getDoctorPassword(@Param("doctorId") String doctorId);

    @Query(value = "SELECT password FROM patients WHERE patient_id = :patientId", nativeQuery = true)
    String getPatientPassword(@Param("patientId") String patientId);

    @Modifying
    @Query(value = "DELETE FROM appointments WHERE appointment_id = :appointmentId AND doctor_id = :doctorId", nativeQuery = true)
    int deleteByDoctor(@Param("appointmentId") int appointmentId, @Param("doctorId") String doctorId);

    @Modifying
    @Query(value = "DELETE FROM appointments WHERE appointment_id = :appointmentId AND patient_id = :patientId", nativeQuery = true)
    int deleteByPatient(@Param("appointmentId") int appointmentId, @Param("patientId") String patientId);
}

