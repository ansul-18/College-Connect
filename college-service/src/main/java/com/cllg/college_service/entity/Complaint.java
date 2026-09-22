package com.cllg.college_service.entity;


import com.cllg.college_service.enums.ComplaintCategory;
import com.cllg.college_service.enums.ComplaintStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "complaints")
@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_id",nullable = false)
    private Long studentId;
    @Column(name = "department_id")
    private Long departmentId;
    @Column(nullable = false,length = 200)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 40)
    private ComplaintCategory complaintCategory;
    @Column(length = 250)
    private String location;
    @Column(name = "image_url",length = 700)
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )

    @Builder.Default
    private ComplaintStatus complaintStatus = ComplaintStatus.PENDING;
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;
    @Column(
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
