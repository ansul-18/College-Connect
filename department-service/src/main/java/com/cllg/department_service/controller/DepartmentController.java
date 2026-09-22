package com.cllg.department_service.controller;

import com.cllg.department_service.dto.DepartmentRequest;
import com.cllg.department_service.dto.DepartmentResponse;
import com.cllg.department_service.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request){
        return ResponseEntity.ok(departmentService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAll(){
        return ResponseEntity.ok(departmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(departmentService.getById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<DepartmentResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(departmentService.getByCode(code));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        departmentService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable Long id,@Valid @RequestBody DepartmentRequest request){
        return ResponseEntity.ok(departmentService.update(id,request));
    }

}
