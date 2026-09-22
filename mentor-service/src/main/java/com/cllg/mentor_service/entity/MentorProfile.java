package com.cllg.mentor_service.entity;

import com.cllg.mentor_service.enums.MentorType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "mentor_profiles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_mentor_user",
                        columnNames = "user_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Auth Service User ID
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Academic
    @Column(name = "department_id")
    private Long departmentId;

    @Column(nullable = false)
    private Integer year;

    // Professional
    @Column(length = 150)
    private String title;

    @Column(length = 100)
    private String experience;

    @Column(length = 150)
    private String company;

    @Column(length = 150)
    private String designation;

    // Profile
    @Column(length = 2000)
    private String bio;

    @Column(length = 500)
    private String profileImage;

    // Social
    @Column(length = 500)
    private String github;

    @Column(length = 500)
    private String linkedin;

    // Mentorship
    @Enumerated(EnumType.STRING)
    @Column(name = "mentor_type", nullable = false)
    private MentorType mentorType;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    // Account
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}