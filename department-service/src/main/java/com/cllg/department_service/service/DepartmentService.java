package com.cllg.department_service.service;

import com.cllg.department_service.dto.DepartmentRequest;
import com.cllg.department_service.dto.DepartmentResponse;
import com.cllg.department_service.entity.Department;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse create(DepartmentRequest request);
    DepartmentResponse getById(Long id);
    List<DepartmentResponse> getAll();
    void delete(Long id);
    DepartmentResponse update(Long id,DepartmentRequest request);
    DepartmentResponse getByCode(String code);

}
