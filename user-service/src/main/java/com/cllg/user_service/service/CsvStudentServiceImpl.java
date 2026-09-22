package com.cllg.user_service.service;

import com.cllg.user_service.client.DepartmentClient;
import com.cllg.user_service.client.DepartmentResponse;
import com.cllg.user_service.dto.request.StudentCsvRow;
import com.cllg.user_service.dto.request.StudentRequest;
import com.cllg.user_service.dto.response.StudentCsvExportRow;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvStudentServiceImpl implements CsvStudentService {

    private final StudentService studentService;
    private final DepartmentClient departmentClient;

    private final CsvMapper csvMapper = new CsvMapper();

    // =========================
    // IMPORT STUDENTS
    // =========================
    @Override
    @Transactional
    public int importStudents(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is required");
        }

        try {

            String csv = new String(file.getBytes());

            CsvSchema schema = CsvSchema.emptySchema().withHeader();

            MappingIterator<StudentCsvRow> iterator = csvMapper
                            .readerFor(StudentCsvRow.class)
                            .with(schema)
                            .readValues(csv);
            List<StudentCsvRow> rows = iterator.readAll();

            int imported = 0;

            for (StudentCsvRow row : rows) {

                // FIND DEPARTMENT BY CODE
                DepartmentResponse department =
                        departmentClient.getDepartmentByCode(row.getDepartmentCode());

                if (department == null) {
                    throw new IllegalArgumentException("Department not found with code: " + row.getDepartmentCode()
                    );
                }

                // CREATE STUDENT REQUEST
                StudentRequest request = new StudentRequest();

                request.setName(row.getName());
                request.setRollNumber(row.getRollNumber());
                request.setEmail(row.getEmail());
                request.setMobile(row.getMobile());

                // Database stores department ID
                request.setDepartmentId(department.getId());

                request.setYear(row.getYear());

                // CREATE STUDENT
                studentService.createStudent(request);

                imported++;
            }

            return imported;

        } catch (IOException ex) {

            throw new IllegalArgumentException("Unable to read CSV file", ex);
        }
    }

    // =========================
    // EXPORT STUDENTS
    // =========================

    @Override
    @Transactional(readOnly = true)
    public byte[] exportStudents() {

        try {

            List<StudentCsvExportRow> rows = studentService
                            .getAllStudents()
                            .stream()
                            .map(student -> {

                                StudentCsvExportRow row = new StudentCsvExportRow();

                                row.setName(student.getName());
                                row.setRollNumber(student.getRollNumber());
                                row.setEmail(student.getEmail());
                                row.setMobile(student.getMobile());

                                // GET DEPARTMENT DETAILS
                                DepartmentResponse department = departmentClient.getDepartmentById(student.getDepartmentId());

                                if (department == null) {
                                    throw new IllegalArgumentException("Department not found with id: " + student.getDepartmentId());
                                }

                                row.setDepartmentCode(department.getCode());

                                row.setDepartmentName(department.getName());

                                row.setYear(student.getYear());

                                row.setAccountStatus(student.getAccountStatus() != null
                                                ? student.getAccountStatus().name() : null);

                                return row;

                            })
                            .toList();

            // CREATE CSV SCHEMA
            CsvSchema schema = csvMapper
                            .schemaFor(StudentCsvExportRow.class)
                            .withHeader();

            // CONVERT TO CSV BYTES
            return csvMapper.writer(schema)
                    .writeValueAsBytes(rows);

        } catch (IOException ex) {

            throw new IllegalStateException("Unable to export students", ex);
        }
    }
}