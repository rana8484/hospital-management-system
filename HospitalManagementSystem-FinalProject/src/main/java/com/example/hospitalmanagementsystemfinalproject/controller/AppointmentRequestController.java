package com.example.hospitalmanagementsystemfinalproject.controller;

import com.example.hospitalmanagementsystemfinalproject.model.AppointmentRequest;
import com.example.hospitalmanagementsystemfinalproject.service.AppointmentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointmentrequests")
public class AppointmentRequestController {

    @Autowired
    AppointmentRequestService appointmentRequestService;

    @GetMapping("/requested")
    public ResponseEntity<List<AppointmentRequest>> getRequestedAppointments(@RequestParam String doctorId) {
        List<AppointmentRequest> requests = appointmentRequestService.getRequestedAppointments(doctorId);
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/accept/{requestId}")
    public ResponseEntity<String> acceptAppointment(@PathVariable int requestId) {
        boolean success = appointmentRequestService.acceptAppointment(requestId);

        if (success) {
            return ResponseEntity.ok("Appointment accepted and saved.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to accept the appointment.");
        }
    }

    @PutMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectAppointment(@PathVariable int requestId) {
        boolean success = appointmentRequestService.rejectAppointment(requestId);

        if (success) {
            return ResponseEntity.ok("Appointment request rejected.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reject the appointment request.");
        }
    }


    @PostMapping("/request")
    public ResponseEntity<String> requestAppointment(
            @RequestParam String doctorId,
            @RequestParam String patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {

        boolean success = appointmentRequestService.requestAppointment(doctorId, date, time, patientId);

        if (success) {
            return ResponseEntity.ok("Appointment request submitted.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Time slot is already taken or user not found.");
        }
    }

}
