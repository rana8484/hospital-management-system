package com.example.hospitalmanagementsystemfinalproject.service;

import com.example.hospitalmanagementsystemfinalproject.model.Appointment;
import com.example.hospitalmanagementsystemfinalproject.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Map<String, Object>> getAppointmentsByPatientId(String patientId) {
        List<Appointment> appts = appointmentRepository.findUpcomingByPatientId(patientId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Appointment appt : appts) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("apptId", appt.getApptId());
            map.put("date", appt.getDate());
            map.put("time", appt.getTime());
            if (appt.getDoctor() != null) {
                map.put("doctorName", appt.getDoctor().getName());
                if (appt.getDoctor().getDepartment() != null) {
                    map.put("department", appt.getDoctor().getDepartment().getDepartmentName());
                } else {
                    map.put("department", "N/A");
                }
            }
            response.add(map);
        }

        return response;
    }

    public List<Map<String, Object>> getAppointmentsByDoctorId(String doctorId) {
        List<Appointment> appts = appointmentRepository.findUpcomingByDoctorId(doctorId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Appointment appt : appts) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("apptId", appt.getApptId());
            map.put("date", appt.getDate());
            map.put("time", appt.getTime());
            if (appt.getPatient() != null) {
                map.put("patientName", appt.getPatient().getName());
                map.put("patientAge", appt.getPatient().getAge());
            }
            response.add(map);
        }

        return response;
    }

    public List<Map<String, Object>> getDailyAppointments(String userId) {
        boolean isDoctor = Character.toUpperCase(userId.charAt(0)) == 'D';
        List<Object[]> results = appointmentRepository.findDailyAppointments(userId, isDoctor);

        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("apptId", row[0]);
            map.put("date", row[1]);
            map.put("time", row[2]);
            map.put("name", row[3]);
            map.put("extraInfo", row[4]);
            response.add(map);
        }
        return response;
    }

    public List<Map<String, Object>> getPreviousAppointmentsByPatientId(String patientId) {
        List<Object[]> results = appointmentRepository.findPreviousAppointmentsByPatientId(patientId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("apptId", row[0]);
            map.put("date", row[1]);
            map.put("time", row[2]);
            map.put("doctorName", row[3]);
            map.put("department", row[4]);
            response.add(map);
        }

        return response;
    }

    @Transactional
    public boolean deleteAppointment(int appointmentId, String userId, String password) {
        boolean isDoctor = userId.toUpperCase().charAt(0) == 'D';
        String storedPassword;

        if (isDoctor) {
            storedPassword = appointmentRepository.getDoctorPassword(userId);
        } else {
            storedPassword = appointmentRepository.getPatientPassword(userId);
        }

        if (storedPassword == null || !storedPassword.equals(password)) {
            return false;
        }

        int rowsAffected = isDoctor ?
                appointmentRepository.deleteByDoctor(appointmentId, userId) :
                appointmentRepository.deleteByPatient(appointmentId, userId);

        return rowsAffected > 0;
    }

}

