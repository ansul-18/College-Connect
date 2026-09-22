package com.cllg.user_service.service;

import com.cllg.user_service.dto.request.LinkAuthAccountRequest;
import com.cllg.user_service.dto.request.StudentRequest;
import com.cllg.user_service.dto.response.StudentResponse;
import com.cllg.user_service.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StudentService {

    // CREATE STUDENT
    StudentResponse createStudent(StudentRequest request);

    // GET STUDENT BY ID
    StudentResponse getStudentById(Long id);

    // GET STUDENT BY AUTH USER ID
    StudentResponse getStudentByAuthUserId(Long authUserId);

    // GET ALL STUDENTS
    List<StudentResponse> getAllStudents();

    // GET BY DEPARTMENT
    List<StudentResponse> getStudentsByDepartment(Long departmentId);

    // GET BY YEAR
    List<StudentResponse> getStudentsByYear(Integer year);

    // GET BY DEPARTMENT + YEAR
    List<StudentResponse> getStudentsByDepartmentAndYear(
            Long departmentId,
            Integer year
    );

    // UPDATE STUDENT
    StudentResponse updateStudent(Long id, StudentRequest request);

    // LINK AUTH ACCOUNT
    StudentResponse linkAuthAccount(
            Long id,
            LinkAuthAccountRequest request
    );

    StudentResponse createMyProfile(
            Long authUserId,
            StudentRequest request
    );

    // DELETE STUDENT
    void deleteStudent(Long id);

    // INTERNAL HELPER
    Student findStudentById(Long id);

    // VALIDATE DEPARTMENT
    void validateDepartment(Long departmentId);


}
