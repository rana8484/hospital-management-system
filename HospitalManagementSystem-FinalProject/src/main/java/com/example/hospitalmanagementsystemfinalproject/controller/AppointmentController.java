package com.example.hospitalmanagementsystemfinalproject.controller;

import com.example.hospitalmanagementsystemfinalproject.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/patient/{id}")
    public List<Map<String, Object>> getAppointmentsByPatientId(@PathVariable String id) {
        return appointmentService.getAppointmentsByPatientId(id);
    }

    @GetMapping("/doctor/{id}")
    public List<Map<String, Object>> getAppointmentsByDoctorId(@PathVariable String id) {
        return appointmentService.getAppointmentsByDoctorId(id);
    }

    @GetMapping("/daily/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getDailyAppointments(@PathVariable String userId) {
        List<Map<String, Object>> appointments = appointmentService.getDailyAppointments(userId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/previous/{patientId}")
    public ResponseEntity<List<Map<String, Object>>> getPreviousAppointments(@PathVariable String patientId) {
        List<Map<String, Object>> result = appointmentService.getPreviousAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAppointment(
            @RequestParam int appointmentId,
            @RequestParam String userId,
            @RequestParam String password) {

        boolean success = appointmentService.deleteAppointment(appointmentId, userId, password);

        if (success) {
            return ResponseEntity.ok("Appointment deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Deletion failed. Wrong password or invalid user.");
        }
    }
}
