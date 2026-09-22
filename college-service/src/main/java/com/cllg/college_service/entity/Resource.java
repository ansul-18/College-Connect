package com.cllg.college_service.entity;

import com.cllg.college_service.enums.ResourceCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "file_url",nullable = false,length = 700)
    private String fileUrl;
    @Column(name = "file_type",length = 50)
    private String filetype;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 40)
    private ResourceCategory resourceCategory;
    @Column(name = "department_id")
    private Long departmentId;

    @Column(nullable = false)
    private Integer year;
    @Column(name = "uploaded_by")
    private Long uploadedBy;
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

}
