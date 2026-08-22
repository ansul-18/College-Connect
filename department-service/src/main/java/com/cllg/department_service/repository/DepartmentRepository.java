package com.cllg.department_service.repository;

import com.cllg.department_service.dto.DepartmentResponse;
import com.cllg.department_service.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

    Optional<Department> findByCodeIgnoreCase(String code);
    Optional<Department> findByNameIgnoreCase(String name);

    boolean existsByCodeIgnoreCase(String code);
    boolean existsByNameIgnoreCase(String name);

    Optional<Department> findDepartmentById(Long id);
}
