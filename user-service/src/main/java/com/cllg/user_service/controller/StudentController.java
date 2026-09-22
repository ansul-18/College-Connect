package com.cllg.user_service.controller;

import com.cllg.user_service.dto.request.LinkAuthAccountRequest;
import com.cllg.user_service.dto.request.StudentRequest;
import com.cllg.user_service.dto.response.StudentResponse;
import com.cllg.user_service.service.StudentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    // =========================================================
    // CREATE STUDENT
    // =========================================================
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @RequestBody StudentRequest request
    ) {

        StudentResponse response =
                studentService.createStudent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // CREATE MY PROFILE
    // =========================================================
    @PostMapping("/me")
    public ResponseEntity<StudentResponse> createMyProfile(
            @RequestBody StudentRequest request,
            JwtAuthenticationToken authentication
    ) {

        // -----------------------------------------------------
        // Get user ID from JWT
        // -----------------------------------------------------
        Long authUserId =
                authentication
                        .getToken()
                        .getClaim("userId");


        // -----------------------------------------------------
        // Validate user ID
        // -----------------------------------------------------
        if (authUserId == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }


        // -----------------------------------------------------
        // Create profile linked with auth user
        // -----------------------------------------------------
        StudentResponse response =
                studentService.createMyProfile(
                        authUserId,
                        request
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET ALL STUDENTS
    // =========================================================
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {

        return ResponseEntity.ok(
                studentService.getAllStudents()
        );
    }


    // =========================================================
    // GET STUDENT BY ID
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                studentService.getStudentById(id)
        );
    }


    // =========================================================
    // GET STUDENT BY AUTH USER ID
    // =========================================================
    @GetMapping("/auth/{authUserId}")
    public ResponseEntity<StudentResponse> getStudentByAuthUserId(
            @PathVariable Long authUserId
    ) {

        return ResponseEntity.ok(
                studentService.getStudentByAuthUserId(
                        authUserId
                )
        );
    }


    // =========================================================
    // GET STUDENTS BY DEPARTMENT
    // =========================================================
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<StudentResponse>>
    getStudentsByDepartment(
            @PathVariable Long departmentId
    ) {

        return ResponseEntity.ok(
                studentService.getStudentsByDepartment(
                        departmentId
                )
        );
    }


    // =========================================================
    // GET STUDENTS BY YEAR
    // =========================================================
    @GetMapping("/year/{year}")
    public ResponseEntity<List<StudentResponse>>
    getStudentsByYear(
            @PathVariable Integer year
    ) {

        return ResponseEntity.ok(
                studentService.getStudentsByYear(year)
        );
    }


    // =========================================================
    // GET STUDENTS BY DEPARTMENT + YEAR
    // =========================================================
    @GetMapping("/department/{departmentId}/year/{year}")
    public ResponseEntity<List<StudentResponse>>
    getStudentsByDepartmentAndYear(
            @PathVariable Long departmentId,
            @PathVariable Integer year
    ) {

        return ResponseEntity.ok(
                studentService.getStudentsByDepartmentAndYear(
                        departmentId,
                        year
                )
        );
    }


    // =========================================================
    // UPDATE STUDENT
    // =========================================================
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequest request
    ) {

        return ResponseEntity.ok(
                studentService.updateStudent(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // LINK AUTH ACCOUNT
    // =========================================================
    @PutMapping("/{id}/link-auth")
    public ResponseEntity<StudentResponse> linkAuthAccount(
            @PathVariable Long id,
            @RequestBody LinkAuthAccountRequest request
    ) {

        return ResponseEntity.ok(
                studentService.linkAuthAccount(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // DELETE STUDENT
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id
    ) {

        studentService.deleteStudent(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}