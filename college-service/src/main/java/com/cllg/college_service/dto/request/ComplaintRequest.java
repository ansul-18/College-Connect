package com.cllg.college_service.dto.request;


import com.cllg.college_service.enums.ComplaintCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.hc.client5.http.impl.ZstdRuntime;

@Data
public class ComplaintRequest {

    @NotNull(message = "Student id is required")

    private Long studentId;
    private Long departmentId;

    @NotBlank(message = "Title is required")
    private String title;


    @NotBlank(message = "Description is required")
    private String description;


    @NotNull(message = "Category is required")
    private ComplaintCategory category;


    private String location;

}
