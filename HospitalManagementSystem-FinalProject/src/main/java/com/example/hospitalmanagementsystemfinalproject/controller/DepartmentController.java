package com.example.hospitalmanagementsystemfinalproject.controller;

import com.example.hospitalmanagementsystemfinalproject.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllDepartmentNames() {
        List<String> departmentNames = departmentService.getAllDepartmentNames();
        return ResponseEntity.ok(departmentNames);
    }
}

