package com.example.hospitalmanagementsystemfinalproject.repository;

import com.example.hospitalmanagementsystemfinalproject.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department findByDepartmentName(String departmentName);

    @Query("SELECT d.departmentName FROM Department d")
    List<String> findAllDepartmentNames();
}
