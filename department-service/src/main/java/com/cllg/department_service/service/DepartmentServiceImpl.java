package com.cllg.department_service.service;

import com.cllg.department_service.dto.DepartmentRequest;
import com.cllg.department_service.dto.DepartmentResponse;
import com.cllg.department_service.entity.Department;
import com.cllg.department_service.exception.ResourceAlreadyExistsException;
import com.cllg.department_service.exception.ResourceNotFoundException;
import com.cllg.department_service.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    @Override
    //Create Department
    public DepartmentResponse create(DepartmentRequest request) {

        String name = request.getName().trim();
        String code = request.getCode().trim().toUpperCase();

        if (repository.existsByNameIgnoreCase(name)) {
            throw new ResourceAlreadyExistsException("Department already exists with name: " + name);
        }

        if (repository.existsByCodeIgnoreCase(code)) {
            throw new ResourceAlreadyExistsException("Department already exists with code: " + code);
        }

        Department department = Department.builder()
                .name(name)
                .code(code)

                .build();

        Department saved = repository.save(department);
        return mapToResponse(saved);
    }

    @Override
    //
    public DepartmentResponse getById(Long id) {
        Department department = findDepartmentById(id);
        return mapToResponse(department);
    }

    //department by id
    private Department findDepartmentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }


    //Response
    private DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .createdAt(department.getCreatedAt())
                .build();
    }

    @Override
    //get all departments
    public List<DepartmentResponse> getAll() {
        return repository.findAll().stream()
                .map(department -> mapToResponse(department))
                .toList();
    }

    @Override
    //delete department
    public void delete(Long id) {
        Department department = findDepartmentById(id);
        repository.delete(department);

    }

    @Override
    //update
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department existingDepartment = findDepartmentById(id);

        String name = request.getName().trim();
        String code = request.getCode().trim().toUpperCase();

        if (!existingDepartment.getName().equalsIgnoreCase(name) && repository.existsByNameIgnoreCase(name)){
            throw new ResourceAlreadyExistsException("Department already exists with name: " + name);
        }
        if (!existingDepartment.getCode().equalsIgnoreCase(code) && repository.existsByCodeIgnoreCase(code)) {
            throw new ResourceAlreadyExistsException("Department already exists with code: " + code);
        }

        existingDepartment.setName(name);
        existingDepartment.setCode(code);

        Department update = repository.save(existingDepartment);

        return mapToResponse(update);


    }
}
