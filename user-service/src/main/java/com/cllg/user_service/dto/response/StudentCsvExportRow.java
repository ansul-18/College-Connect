package com.cllg.user_service.dto.response;

import lombok.Data;

@Data
public class StudentCsvExportRow {
    private String name;
    private String email;
    private String rollNumber;
    private String mobile;
    private String departmentCode;

    private String departmentName;

    private Integer year;

    private String accountStatus;
}
