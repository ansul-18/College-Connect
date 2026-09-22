package com.cllg.user_service.repository;

import com.cllg.user_service.entity.Student;
import com.cllg.user_service.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    // FIND STUDENT
    Optional<Student> findByEmailIgnoreCase(String email);
    Optional<Student> findByRollNumberIgnoreCase(String rollNUmber);
    Optional<Student> findByEmailIgnoreCaseAndRollNumberIgnoreCase(String email,Long id);
    Optional<Student> findByAuthUserId(Long authUserId);
    // CHECK EXISTENCE
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByRollNumberIgnoreCase(String rollNumber);
    boolean existsByAuthUserId(Long authUserId);

    // FILTER STUDENTS
    List<Student> findByDepartmentId(Long departmentId);
    List<Student> findByYear(Integer year);
    List<Student> findByDepartmentIdAndYear(Long departmentId,Integer year);
    List<Student> findByAccountStatus(AccountStatus accountStatus);

    Student findStudentById(Long id);

}