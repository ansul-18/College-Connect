package com.cllg.user_service.dto.request;

import lombok.*;

@Data
@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor@Builder
public class StudentCsvRow {
    private String name;

    private String rollNumber;

    private String email;

    private String mobile;

    private String departmentCode;

    private Integer year;
}
