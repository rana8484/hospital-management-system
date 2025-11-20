package com.example.hospitalmanagementsystemfinalproject.controller;

import com.example.hospitalmanagementsystemfinalproject.service.DoctorNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctornotes")
public class DoctorNoteController {

    @Autowired
    private DoctorNoteService doctorNoteService;

    @PostMapping("/save")
    public ResponseEntity<String> saveNote(
            @RequestParam String doctorId,
            @RequestParam String patientId,
            @RequestParam String note) {

        if (doctorId == null || patientId == null || note == null || note.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Missing or invalid fields");
        }

        doctorNoteService.saveDoctorNote(doctorId, patientId, note);
        return ResponseEntity.ok("Note saved successfully.");
    }

    @GetMapping("/getptnotes")
    public ResponseEntity<List<Map<String, String>>> getPatientNotes(
            @RequestParam String doctorId,
            @RequestParam String patientId) {

        List<Map<String, String>> notes = doctorNoteService.getPatientNotes(doctorId, patientId);
        return ResponseEntity.ok(notes);
    }
}

